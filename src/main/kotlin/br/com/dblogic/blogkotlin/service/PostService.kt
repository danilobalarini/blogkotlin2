package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostImage
import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.model.facade.FrontPageFacade
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.model.facade.TagFacade
import br.com.dblogic.blogkotlin.repository.PostRepository
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.time.Instant

@Service
class PostService {

    private val logger = LoggerFactory.getLogger(PostService::class.java)

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var postImageService: PostImageService

    @Autowired
    lateinit var tagService: TagService

    @Autowired
    lateinit var blogUtils: BlogUtils

    @Value("\${blog.directory.name}")
    lateinit var rootFolder: String

    fun findAll(): List<Post> {
        return postRepository.findAll()
    }

    fun returnAllFacades(): List<PostFacade> {
        val facades = mutableListOf<PostFacade>()

        for (p in postRepository.findAll()) {
            facades.add(postToFacade(p))
        }

        return facades
    }

    fun findById(id: Long): Post {
        return postRepository.findById(id).get()
    }

    fun findByTag(tagname: String): List<PostFacade> {

        val tag = tagService.findByName(tagname)
        val posts = postRepository.findAllByTags(tag)
        val facades = mutableListOf<PostFacade>()

        for (p in posts) {
            facades.add(postToFacade(p))
        }

        return facades
    }

    fun save(post: Post): Post {
        return postRepository.save(post)
    }

    fun deleteById(id: Long) {
        postRepository.deleteById(id)
    }

    fun frontPage(): FrontPageFacade {
        logger.info("posts zero length? " + (postRepository.count() == 0L));
        logger.info("posts length: ${postRepository.count()}");
        val posts = cleanHtml(postRepository.findTop6ByIsDraftFalseOrderByCreatedAtDesc())
        logger.info("### posts ###: " + posts.size)

        if (posts.isEmpty()) {
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
            val first = posts.first()
            val facade = postToFacade(first)

            var listFacades = mutableListOf<PostFacade>()

            for (p in posts.drop(1)) {
                listFacades.add(postToFacade(p))
            }

            return FrontPageFacade(facade, listFacades)
        }
    }

    fun goArticle(id: Long): PostFacade {
        return postToFacade(findById(id))
    }

    fun allPosts(): List<PostFacade> {
        val posts = cleanHtml(postRepository.findAll())

        var listPostComments = mutableListOf<PostFacade>()

        for (p in posts.drop(1)) {
            listPostComments.add(postToFacade(p))
        }

        return listPostComments

    }

    fun getAllPosts(pageNumber: Int = 0, pageSize: Int = 10): List<PostFacade> {

        val paging = PageRequest.of(pageNumber, pageSize)
        val pagedResult = postRepository.findAll(paging)

        val posts = mutableListOf<PostFacade>()

        for (p in pagedResult.toList()) {
            posts.add(postToFacade(p))
        }

        return posts
    }

    fun brandNewPost(): Post {
        var post = postRepository.save(Post())
        val postImage = PostImage("${post.id}-coverimage.jpg", "default cover image", postImageService.defaultCoverImage(post.title), true, post)
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

            post.text = StringUtils.replace(post.text, oldTitle, newTitle)
        } else {
            post.text = p.text
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

    fun postToFacade(p: Post): PostFacade {

        var tags = tagService.toSetFacade(p.tags)

        return PostFacade(p.id,
                          p.title,
                          p.text,
                          p.isDraft,
                          p.createdAt,
                          p.comments.size,
                          tags,
                          createCoverImage(p))
    }

    private fun cleanHtml(posts: List<Post>): List<Post> {

        var listPost = mutableListOf<Post>()
        for (p in posts) {
            p.text = p.text.replace("</p><p>", " ")
            p.text = p.text.replace("<br/>", " ")
            p.text = p.text.replace("<br>", " ")
            p.text = p.text.replace("\\<.*?>".toRegex(), "")
            listPost.add(p)
        }
        return posts;
    }

}