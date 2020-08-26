package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Comment
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.CommentFacade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface CommentRepository : JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
	
	fun countByPost(post: Post): Long

	fun findByPost(post: Post): List<Comment>

	fun findByPostAndIsApprovedTrue(post: Post): List<Comment>

}