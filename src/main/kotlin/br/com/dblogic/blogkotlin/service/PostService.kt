package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.exception.DeletePostException
import br.com.dblogic.blogkotlin.exception.DeleteTagException
import br.com.dblogic.blogkotlin.model.CaptchaEvent
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostImage
import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.model.facade.FrontPageFacade
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.model.facade.PostSearchFacade
import br.com.dblogic.blogkotlin.model.facade.TagFacade
import br.com.dblogic.blogkotlin.repository.PostRepository
import br.com.dblogic.blogkotlin.repository.specification.PostSpecification
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.apache.commons.lang3.StringUtils
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

    @Value("\${blog.text.length}")
    var textLength: Int = 0

    fun findAll(): List<Post> {
        return postRepository.findAll()
    }

    fun returnAllFacades(): List<PostFacade> {
        return postToFacade(postRepository.findAll())
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
                                postToFacade(all) as MutableList<PostFacade>)
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
                                postToFacade(all) as MutableList<PostFacade>)
    }

    fun save(post: Post): Post {
        return postRepository.save(post)
    }

    fun deleteById(id: Long) {
        postRepository.deleteById(id)
    }

    fun delete(id: Long) {
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

        if(posts.isEmpty()) {
            val facade = PostFacade(0,
                                    "Não temos posts. Volte outro dia. Teremos bolinhos.",
                                    "",
                                    false,
                                    Instant.now(),
                                    0,
                                    mutableSetOf<TagFacade>(),
                                    "")

            return FrontPageFacade(facade, mutableListOf<PostFacade>())
        } else {
            return FrontPageFacade(postToFacade(posts.first()),
                                   postToFacade(posts.drop(1)))
        }
    }

    fun brandNewPost(): Post {
        var post = postRepository.save(Post())
        val postImage = PostImage("${post.id}-coverimage.jpg",
                         "default cover image",
                                   postImageService.defaultCoverImage(post.title),
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

    fun createImageURL(post: Post, imageName: String): String {
        val directoryName = blogUtils.getDirectoryNameFromPost(post)
        return "$rootFolder/$directoryName/$imageName"
    }

    fun updateComposer(p: PostFacade): PostFacade {
        logger.info("updateComposer: ${p.id}")

        var post = findById(p.id)

        if (!StringUtils.equals(post.title, p.title)) {
            logger.info("the title has been changed")

            val olddir = blogUtils.getDirectoryPathFromPost(post)
            logger.info("olddir $olddir")

            post.title = p.title
            val newdir = blogUtils.getDirectoryPathFromPost(post)
            logger.info("newdir $newdir")

            logger.info("renaming directory")
            Files.move(olddir, newdir, StandardCopyOption.REPLACE_EXISTING)

            val oldTitle = StringUtils.difference(newdir.toString(), olddir.toString())
            val newTitle = StringUtils.difference(olddir.toString(), newdir.toString())

            post.review = StringUtils.replace(post.review, oldTitle, newTitle)
        } else {
            post.review = p.review
        }

        post.isDraft = p.isDraft

        val savepost = postRepository.save(post)

        return postToFacade(savepost)
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

    fun sumPageView(id: Long) {
        postRepository.sumPageView(id)
    }

    fun mostVisitedPosts(): List<PostFacade> {
        return postToFacade(postRepository.findTop2ByIsDraftFalseOrderByPageviewDesc())
    }

    fun postToFacade(p: Post): PostFacade {

        return PostFacade(p.id,
                          p.title,
                          safeTruncate(p.review),
                          p.isDraft,
                          p.createdAt,
                          p.comments.size,
                          tagService.toSetFacade(p.tags),
                          createCoverImage(p))
    }

    fun postToFacade(posts: List<Post>): List<PostFacade> {
        val postList = mutableListOf<PostFacade>()
        for (p in posts) {
            postList.add(PostFacade(p.id,
                         p.title,
                         safeTruncate(p.review),
                         p.isDraft,
                         p.createdAt,
                         p.comments.size,
                         tagService.toSetFacade(p.tags),
                         createCoverImage(p)))
        }
        return postList
    }

    private fun postToFacade(posts: Page<Post>): List<PostFacade> {
        val postList = mutableListOf<PostFacade>()
        for (p in posts) {
            postList.add(PostFacade(p.id,
                         p.title,
                         Jsoup.parse(p.review).text(),
                         p.isDraft,
                         p.createdAt,
                         p.comments.size,
                         tagService.toSetFacade(p.tags),
                         createCoverImage(p)))
        }
        return postList
    }

    private fun cleanHtml(posts: List<Post>): List<Post> {

        var listPost = mutableListOf<Post>()
        for (p in posts) {
            p.review = Jsoup.parse(p.review).text()
            listPost.add(p)
        }
        return posts;
    }

    private fun safeTruncate(text: String): String {
        val words = StringUtils.split(text, StringUtils.SPACE)
        var safetrunc = ""
        for(x in 0 until words.size) {
            safetrunc = safetrunc.plus(StringUtils.SPACE + words[x])
        }
        return safetrunc
    }

}