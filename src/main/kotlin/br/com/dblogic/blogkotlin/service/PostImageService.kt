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
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import javax.imageio.ImageIO

@Service
class PostImageService {

    private val logger = LoggerFactory.getLogger(PostImageService::class.java)

    @Autowired
    lateinit var postService: PostService

    @Autowired
    lateinit var blogUtils: BlogUtils

    @Autowired
    lateinit var postImageRepository: PostImageRepository

    @Value("\${blog.directory.name}")
    lateinit var blogDirectoryName: String

    @Value("\${server.port}")
    lateinit var port: String

    fun findById(id: Long) : PostImage {
        return postImageRepository.findById(id).get()
    }

    fun findCoverImage(post: Post): PostImage {
        return postImageRepository.findByPostAndIsCoverImageTrue(post)
    }

    fun save(id: Long, multiPartFile: MultipartFile): String {

        val post = postService.findById(id)
        val name = getName(multiPartFile)
        val image = if (multiPartFile.bytes.isNotEmpty()) multiPartFile.bytes else byteArrayOf(0)

        val postImage = postImageRepository.save(PostImage(id, name, "", image, false, post))

        val title = blogUtils.getDirectoryNameFromPost(post)
        logger.info("### title: $title")

        val newimage = Paths.get(blogUtils.appendToBlogDir("$title/$name"))
        logger.info("### path to be created: $newimage")
        Files.write(newimage, postImage.image, StandardOpenOption.CREATE)

        val url = "../$blogDirectoryName/$title/$name"
        logger.info("URL: $url")

        return url
    }

    private fun getName(multiPartFile: MultipartFile) : String {

        if(!multiPartFile.isEmpty) {
            if(multiPartFile.originalFilename?.isNotBlank()!!) {
                return multiPartFile.originalFilename!!
            }
        }
        return ""
    }

    fun updateFrontPageCoverImage(id: Long, multiPartFile: MultipartFile) {

        val post = postService.findById(id)
        val title = blogUtils.getDirectoryNameFromPost(post)

        val imageName = postImageRepository.findByPostAndIsCoverImageTrue(post).filename
        val imagepath = Paths.get(blogUtils.appendToBlogDir("$title/$imageName"))
        logger.info("### path to be deleted: $imagepath")
        Files.delete(imagepath)

        val postImage = updateCoverImage(post.id, multiPartFile)
        val newimage = Paths.get(blogUtils.appendToBlogDir("$title/${postImage.filename}"))
        logger.info("path to be created: $newimage")

        Files.write(newimage, postImage.image, StandardOpenOption.CREATE)
    }

    fun updateCoverImage(id: Long, coverImage: MultipartFile): PostImage {

        val post = postService.findById(id)

        val deleteImage = postImageRepository.findByPostAndIsCoverImageTrue(post)
        val name = getName(coverImage)
        val image = if (coverImage.bytes.isNotEmpty()) coverImage.bytes else byteArrayOf(0)

        val postImage = PostImage(name, "", image,  true, post)

        post.addPostImage(postImage)
        post.removePostImage(deleteImage)

        return postImageRepository.save(postImage)
    }

    fun defaultCoverImage(text: String): ByteArray {

        val image = BufferedImage(1300, 860, BufferedImage.TYPE_INT_RGB)
        val g2d: Graphics2D = image.createGraphics()
        g2d.setColor(Color.WHITE)
        g2d.fillRect(0, 0, 1300, 860)

        g2d.setColor(Color.BLACK)
        val font = Font("Georgia", Font.BOLD, 36);
        g2d.setFont(font);
        g2d.drawString(text, 200, 200)

        val baos = ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush()
        val imageBA = baos.toByteArray()
        baos.close()

        return imageBA;
    }

}