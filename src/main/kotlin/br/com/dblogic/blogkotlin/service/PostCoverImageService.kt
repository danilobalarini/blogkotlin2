package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.config.InitialConfiguration
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostCoverImage
import br.com.dblogic.blogkotlin.repository.PostCoverImageRepository
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.Color
import java.awt.Font

import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import javax.imageio.ImageIO

@Service
class PostCoverImageService {

	private val logger = LoggerFactory.getLogger(PostCoverImageService::class.java)

	@Autowired
	lateinit var postCoverImageRepository: PostCoverImageRepository

	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var blogUtils: BlogUtils

	fun findById(id: Long) : PostCoverImage {
		return postCoverImageRepository.findById(id).get()
	}

	fun findByPost(p: Post): PostCoverImage {
		return postCoverImageRepository.findByPost(p)
	}

    fun save(postCoverImage: PostCoverImage) {
		postCoverImageRepository.save(postCoverImage)
	}

	fun update(id: Long, coverImage: MultipartFile): PostCoverImage {

		val post = Post(id)
		val name = getName(coverImage)
		val image = if (coverImage.bytes.isNotEmpty()) coverImage.bytes else byteArrayOf(0)

		val postCoverImage = PostCoverImage(id, name, image, post)

		return postCoverImageRepository.save(postCoverImage)
	}

	fun update(post: Post, coverImage: MultipartFile) : PostCoverImage {

		val name = getName(coverImage)
		val image = if (coverImage.bytes.isNotEmpty()) coverImage.bytes else byteArrayOf(0)

		val postCoverImage = PostCoverImage(post.id, name, image, post)

		return postCoverImageRepository.save(postCoverImage)
	}

	fun updateFrontPageCoverImage(id: Long, multiPartFile: MultipartFile) {

		val post = postService.findById(id)
		val title = blogUtils.getDirectoryNameFromPost(post)

		val imageName = postCoverImageRepository.findByPost(post).filename
		val imagepath = Paths.get(blogUtils.appendToBlogDir("$title/$imageName"))
		logger.info("### path to be deleted: $imagepath")
		Files.delete(imagepath)

		val postCoverImage = update(post.id, multiPartFile)
		val newimage = Paths.get(blogUtils.appendToBlogDir("$title/${postCoverImage.filename}"))
		logger.info("path to be created: $newimage")
		Files.write(newimage, postCoverImage.coverImage, StandardOpenOption.CREATE)
	}

	private fun getName(coverImage: MultipartFile) : String {

		if(!coverImage.isEmpty) {
			if(coverImage.originalFilename?.isNotBlank()!!) {
				return coverImage.originalFilename!!
			}
		}
		return ""
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