package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostCoverImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostCoverImageRepository : JpaRepository<PostCoverImage, Long> {

	fun countById(id: Long): Long

	override fun findById(id: Long): Optional<PostCoverImage>

	override fun deleteById(id: Long)

	fun findByPost(post: Post): PostCoverImage

}