package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.awt.print.Pageable
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>, PagingAndSortingRepository<Post, Long> {

	override fun findById(id: Long): Optional<Post>

	override fun deleteById(id: Long)

	fun findByOrderByCreatedAtDesc(): List<Post>

	//fun findAll(pageable: Pageable)
}