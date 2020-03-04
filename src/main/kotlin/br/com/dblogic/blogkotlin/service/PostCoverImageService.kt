package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostCoverImage
import br.com.dblogic.blogkotlin.repository.PostCoverImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PostCoverImageService {

	@Autowired
	lateinit var postCoverImageRepository: PostCoverImageRepository

	fun findById(id: Long) : PostCoverImage {
		return postCoverImageRepository.findById(id).get()
	}

    fun update(id: Long, coverImage: MultipartFile) {

		val post = Post(id)
		val name = getName(coverImage)
		val image = if (coverImage.bytes.isNotEmpty()) coverImage.bytes else byteArrayOf(0)

		val postCoverImage = PostCoverImage(id, name, image, post)

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

}