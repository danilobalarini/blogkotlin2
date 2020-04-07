package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostCoverImage
import br.com.dblogic.blogkotlin.repository.PostCoverImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.Color
import java.awt.Font

import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class PostCoverImageService {

	@Autowired
	lateinit var postCoverImageRepository: PostCoverImageRepository

	fun findById(id: Long) : PostCoverImage {
		return postCoverImageRepository.findById(id).get()
	}

    fun save(postCoverImage: PostCoverImage) {
		postCoverImageRepository.save(postCoverImage)
	}

    fun update(id: Long, coverImage: MultipartFile) {

		val post = Post(id)
		val name = getName(coverImage)
		val image = if (coverImage.bytes.isNotEmpty()) coverImage.bytes else byteArrayOf(0)

		val postCoverImage = PostCoverImage(id, name, "", image, post)

		postCoverImageRepository.save(postCoverImage)
	}

	fun findByPost(p: Post): PostCoverImage {
		return postCoverImageRepository.findByPost(p)
	}

	private fun getName(coverImage: MultipartFile) : String {

		if(!coverImage.isEmpty) {
			if(coverImage.originalFilename?.isNotBlank()!!) {
				return coverImage.originalFilename!!
			}
		}
		return ""
	}

    fun defaultCoverImage(): ByteArray {

        val image = BufferedImage(1300, 860, BufferedImage.TYPE_INT_RGB)
        val g2d: Graphics2D = image.createGraphics()
        g2d.setColor(Color.WHITE)
        g2d.fillRect(0, 0, 1300, 860)

        g2d.setColor(Color.BLACK)
        val font = Font("Georgia", Font.BOLD, 36);
        g2d.setFont(font);
        g2d.drawString("Algo deve ser escrito em algum lugar", 200, 200)

        val baos = ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush()
        val imageBA = baos.toByteArray()
        baos.close()

        return imageBA;
    }

}