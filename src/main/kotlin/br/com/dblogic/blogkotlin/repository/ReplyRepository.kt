package br.com.dblogic.blog.repository

import org.springframework.stereotype.Repository
import br.com.dblogic.blog.model.Reply
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import br.com.dblogic.blog.model.Post

@Repository
interface ReplyRepository : JpaRepository<Reply, Long>, JpaSpecificationExecutor<Reply> {	
	
	fun findByPost(post: Post) : List<Reply>
	
}