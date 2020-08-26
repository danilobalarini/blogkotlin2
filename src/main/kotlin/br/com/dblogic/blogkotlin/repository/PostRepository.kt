package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import javax.transaction.Transactional

@Repository
interface PostRepository : JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>, PagingAndSortingRepository<Post, Long> {

	override fun findById(id: Long): Optional<Post>

	override fun deleteById(id: Long)
	
	fun findTop6ByIsDraftFalseOrderByCreatedAtDesc(): List<Post>

	fun findTop2ByIsDraftFalseOrderByPageviewDesc(): List<Post>

	fun findByIsDraftFalseOrderByCreatedAtDesc(): List<Post>

	fun findByIsDraftFalseOrderByCreatedAtDesc(pageable: Pageable): Page<Post>

	fun findAllByTags(tag: Tag): List<Post>

	@Query(" UPDATE Post p " +
 		   " SET p.pageview = (p.pageview + 1) " +
		   " WHERE p.id = :id")
	@Modifying
	@Transactional
	fun sumPageView(id: Long)

}