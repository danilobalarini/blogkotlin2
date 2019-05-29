package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Comment
import br.com.dblogic.blogkotlin.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
	
	fun countByPost(post: Post): Long
	
}