package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional

@Repository
interface PostRepository : JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

	override fun findById(id: Long): Optional<Post>

	override fun deleteById(id: Long)

	fun findTop6ByOrderByCreatedAtAsc(): List<Post>

	fun findByOrderByCreatedAtDesc(): List<Post>

}