package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostImage
import br.com.dblogic.blogkotlin.repository.PostImageRepository
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class PostImageService {

    private val logger = LoggerFactory.getLogger(PostImageService::class.java)

    @Autowired
    lateinit var blogUtils: BlogUtils

    @Autowired
    lateinit var postImageRepository: PostImageRepository

    @Value("\${server.port}")
    lateinit var port: String

    @Value("\${blog.directory.name}")
    lateinit var rootFolder: String

    fun findById(id: Long) : PostImage {
        return postImageRepository.findById(id).get()
    }

    fun findCoverImage(post: Post): PostImage {
        logger.info("### findCoverImage post: ${post.id}")
        val image = findByPostAndIsCoverImageTrue(post)
        logger.info("### findCoverImage ran successfully")
        return image
    }

    fun updateCoverImage2(post: Post, coverImage: MultipartFile): PostImage {
        logger.info("entrou no updateCoverImage")

        val name = getName(coverImage)
        val image = if (coverImage.bytes.isNotEmpty()) coverImage.bytes else byteArrayOf(0)

        return postImageRepository.save(PostImage(name,
                                        "",
                                                  image,
                                       true,
                                                  post))
    }

    fun save(post: Post, multiPartFile: MultipartFile): String {

        val name = getName(multiPartFile)
        val image = if (multiPartFile.bytes.isNotEmpty()) multiPartFile.bytes else byteArrayOf(0)

        val postImage = postImageRepository.save(PostImage(name, "", image, false, post))

        val title = blogUtils.getDirectoryNameFromPost(post)
        logger.info("### title: $title")

        val newimage = Paths.get(blogUtils.appendToBlogDir("$title/$name"))
        logger.info("### path to be created: $newimage")
        Files.write(newimage, postImage.image, StandardOpenOption.CREATE)

        val url = "../${createImageURL(post, name)}"
        logger.info("URL: $url")

        return url
    }

    fun findByPost(post: Post): List<PostImage> {
        return postImageRepository.findByPost(post)
    }

    private fun getName(multiPartFile: MultipartFile) : String {

        if(!multiPartFile.isEmpty) {
            if(multiPartFile.originalFilename?.isNotBlank()!!) {
                return blogUtils.stripAccents(multiPartFile.originalFilename!!)
            }
        }
        return ""
    }

    private fun createImageURL(post: Post, imageName: String): String {
        val directoryName = blogUtils.getDirectoryNameFromPost(post)
        return "$rootFolder/$directoryName/$imageName"
    }

    private fun createCoverImage(post: Post): String {
        logger.info("entering createCoverImage")
        val directoryName = blogUtils.getDirectoryNameFromPost(post)
        logger.info("set up directoryName: $directoryName")
        val imageName = findCoverImage(post).filename
        logger.info("set up imageName: $imageName")

        val url = "$rootFolder/$directoryName/$imageName"
        logger.info("set up url: $url")

        return url
    }

    private fun findByPostAndIsCoverImageTrue(post: Post) : PostImage {
        return postImageRepository.findByPostAndIsCoverImageTrue(post)
    }


}