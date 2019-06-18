package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Comment
import br.com.dblogic.blogkotlin.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {
	
	@Autowired
	lateinit var commentRepository: CommentRepository
	
	fun count(): Long {
		return commentRepository.count();
	}
	
	fun findAll(): List<Comment> {
		return commentRepository.findAll()
	}
	
	fun save(comment: Comment) : Comment {
		return commentRepository.save(comment)
	}
	
}