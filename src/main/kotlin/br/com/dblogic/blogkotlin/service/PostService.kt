package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.exception.DeletePostException
import br.com.dblogic.blogkotlin.model.CaptchaEvent
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostImage
import br.com.dblogic.blogkotlin.model.facade.FrontPageFacade
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.model.facade.PostSearchFacade
import br.com.dblogic.blogkotlin.model.facade.TagFacade
import br.com.dblogic.blogkotlin.repository.PostRepository
import br.com.dblogic.blogkotlin.repository.specification.PostSpecification
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.time.Instant

import org.jsoup.Jsoup
import java.util.stream.Collectors
import kotlin.streams.asSequence

@Service
class PostService {

    private val logger = LoggerFactory.getLogger(PostService::class.java)

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var postSpecification: PostSpecification

    @Autowired
    lateinit var postImageService: PostImageService

    @Autowired
    lateinit var tagService: TagService

    @Autowired
    lateinit var blogUtils: BlogUtils

    @Autowired
    lateinit var captchaService: CaptchaService

    @Value("\${blog.directory.name}")
    lateinit var rootFolder: String

    @Value("\${blog.archive.pagesize}")
    var pagesize: Int = 0

    @Value("\${blog.all.pagesize}")
    var pagesizeAll: Int = 0

    fun findAll(): List<Post> {
        return postRepository.findAll()
    }

    fun findById(id: Long): Post {
        return postRepository.findById(id).get()
    }

    fun findByTag(tagname: String): List<PostFacade> {
        val tag = tagService.findByName(tagname)
        return postToFacade(postRepository.findAllByTags(tag))
    }

    fun findByTitleOrReviewOrderByCreatedAt(postSearchFacade: PostSearchFacade): PostSearchFacade {
        logger.info("saving captcha response of search")
        captchaService.save(postSearchFacade.response, CaptchaEvent.SEARCH, 0)

        val pageable = PageRequest.of(postSearchFacade.pagenumber, pagesize)
        val post = Post(postSearchFacade.title, postSearchFacade.review)
        val specification = postSpecification.findByTitleOrReviewOrderByCreatedAt(post)
        val all = postRepository.findAll(specification, pageable)

        return PostSearchFacade(postSearchFacade.title,
                                postSearchFacade.review,
                                "",
                                postSearchFacade.pagenumber,
                                all.isFirst,
                                all.isLast,
                                all.totalPages,
                                postToFacadeCleanHtml(all) as MutableList<PostFacade>)
    }

    fun findAllCreatedAt(postSearchFacade: PostSearchFacade): PostSearchFacade {
        logger.info("find by findAllCreatedAt")

        val pageable = PageRequest.of(postSearchFacade.pagenumber, pagesizeAll)
        val all = postRepository.findByIsDraftFalseOrderByCreatedAtDesc(pageable)

        return PostSearchFacade(postSearchFacade.title,
                                postSearchFacade.review,
                                "",
                                postSearchFacade.pagenumber,
                                all.isFirst,
                                all.isLast,
                                all.totalPages,
                                postToFacadeCleanHtml(all) as MutableList<PostFacade>)
    }

    fun save(post: Post): Post {
        return postRepository.save(post)
    }

    fun deleteById(id: Long) {
        try {
            postRepository.deleteById(id)
        } catch (e: Exception) {
            throw DeletePostException("Ocorreu um erro ao tentar apagar o post")
        }
    }

    fun frontPage(): FrontPageFacade {
        logger.info("posts zero length? " + (postRepository.count() == 0L));
        logger.info("posts length: ${postRepository.count()}");
        val posts = cleanHtml(postRepository.findTop6ByIsDraftFalseOrderByCreatedAtDesc())
        logger.info("### posts ###: " + posts.size)

        return if(posts.isEmpty()) {
                    val facade = PostFacade(0,
                        "Não temos posts. Volte outro dia. Teremos bolinhos.",
                        "",
                        false,
                        Instant.now(),
                        0,
                        mutableSetOf<TagFacade>(),
                        "")

                    FrontPageFacade(facade, mutableListOf<PostFacade>())
               } else {
                    FrontPageFacade(postToFacade(posts.first()),
                                    postToFacade(posts.drop(1)))
               }
    }

    fun brandNewPost(title: String): Post {
        var post = postRepository.save(Post(title))
        val postImage = PostImage("${post.id}-coverimage.jpg",
            "default cover image",
            blogUtils.defaultCoverImage(post.title),
            true, post)
        post.addPostImage(postImage)

        val directory = blogUtils.getDirectoryNameFromPost(post)
        logger.info("creating directory $directory")
        val dirpath = blogUtils.appendToBlogDir(blogUtils.getDirectoryNameFromPost(post))
        logger.info("creating dirpath $dirpath")
        File("$dirpath").mkdirs()

        logger.info("filename ${postImage.filename}")
        val filepath = Paths.get("$dirpath/${postImage.filename}")
        Files.write(filepath, postImage.image, StandardOpenOption.CREATE)

        return postRepository.save(post)
    }

    fun createCoverImage(post: Post): String {
        logger.info("entering createCoverImage")
        val directoryName = blogUtils.getDirectoryNameFromPost(post)
        logger.info("set up directoryName: $directoryName")
        val imageName = postImageService.findCoverImage(post).filename
        logger.info("set up imageName: $imageName")

        val url = "$rootFolder/$directoryName/$imageName"
        logger.info("set up url: $url")

        return url
    }

    fun updateCoverImage(post: Post, postImage: PostImage, deleteImage: PostImage) : String {

        val deleteFilename = deleteImage.filename
        logger.info("deleteFilename: $deleteFilename")

        post.addPostImage(postImage)
        post.removePostImage(deleteImage)
        save(post)

        val title = blogUtils.getTitle(post.id, post.title, post.createdAt)
        val imagepath = Paths.get(blogUtils.appendToBlogDir("$title/${postImage.filename}"))
        val imagepathdelete = Paths.get(blogUtils.appendToBlogDir("$title/${deleteFilename}"))
        logger.info("### imagepath: $imagepath")
        logger.info("### imagepathdelete: $imagepathdelete")
        Files.write(imagepath, postImage.image, StandardOpenOption.CREATE)
        Files.delete(imagepathdelete)

        return "../${createCoverImage(post)}"
    }

    fun updateComposer(p: PostFacade): PostFacade {
        logger.info("updateComposer: ${p.id}")

        var post = findById(p.id)

        if (post.title != p.title) {
            logger.info("the title has been changed")

            val olddir = blogUtils.getDirectoryPathFromPost(post)
            logger.info("olddir $olddir")

            post.title = p.title
            val newdir = blogUtils.getDirectoryPathFromPost(post)
            logger.info("newdir $newdir")

            logger.info("renaming directory")
            Files.move(olddir, newdir, StandardCopyOption.REPLACE_EXISTING)
        }
        post.review = p.review
        post.isDraft = p.isDraft

        return postToFacade(postRepository.save(post))
    }

    fun updateTags(postFacade: PostFacade) {
        logger.info("updating tags")

        var post = findById(postFacade.id)
        post.tags.clear()
        post = save(post)

        for (t in postFacade.tags) {
            post.addTag(tagService.findById(t.id))
        }

        logger.info("saving!!!")
        save(post)
    }

    fun insertTag(idpost: Long, idtag: Long) {
        var post = postRepository.findById(idpost)
        val tags = tagService.findByPostId(post.get().id)

        for (t in tags) {
            post.get().addTag(tagService.findById(t.id))
        }
        post.get().addTag(tagService.findById(idtag))

        postRepository.save(post.get())
    }

    fun removeTag(idpost: Long, idtag: Long) {
        var post = postRepository.findById(idpost)
        val tags = tagService.findByPostId(post.get().id)

        for (t in tags) {
            post.get().addTag(tagService.findById(t.id))
        }
        post.get().removeTag(tagService.findById(idtag))

        postRepository.save(post.get())
    }

    fun sumPageView(id: Long) {
        postRepository.sumPageView(id)
    }

    fun mostVisitedPosts(): List<PostFacade> {
        return postToFacade(postRepository.findTop2ByIsDraftFalseOrderByPageviewDesc())
    }

    fun postToFacade(p: Post): PostFacade {
        return toFacade(p)
    }

    fun postToFacade(posts: List<Post>): List<PostFacade> {
        return posts
            .stream()
            .map { p -> toFacade(p) }
            .collect(Collectors.toList())
    }

    fun postToFacadeCleanHtml(posts: Page<Post>): List<PostFacade> {
        return posts
            .stream()
            .map { p -> toFacadeCleanHtml(p) }
            .collect(Collectors.toList())
    }

    private fun postToFacade(posts: Page<Post>): List<PostFacade> {
        return posts
                .stream()
                    .map { p -> toFacade(p) }
                    .collect(Collectors.toList())
    }

    private fun cleanHtml(posts: List<Post>): List<Post> {
        return posts
                .stream()
                    .asSequence()
                    .onEach { p -> p.review = Jsoup.parse(p.review).text() }
                    .toList()
    }

    private fun toFacade(p: Post): PostFacade {
        return PostFacade(p.id,
                          p.title,
                          p.review,
                          p.isDraft,
                          p.createdAt,
                          p.comments.size,
                          tagService.toSetFacade(p.tags),
                          createCoverImage(p))
    }

    private fun toFacadeCleanHtml(p: Post): PostFacade {
        return PostFacade(p.id,
                          p.title,
                          Jsoup.parse(p.review).text(),
                          p.isDraft,
                          p.createdAt,
                          p.comments.size,
                          tagService.toSetFacade(p.tags),
                          createCoverImage(p))
    }

}