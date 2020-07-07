package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Comment
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.CommentFacade
import br.com.dblogic.blogkotlin.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {
	
	@Autowired
	lateinit var commentRepository: CommentRepository

	fun count(): Long {
		return commentRepository.count()
	}
	
	fun findAll(): List<Comment> {
		return commentRepository.findAll()
	}
	
	fun save(comment: Comment) : Comment {
		return commentRepository.save(comment)
	}

	fun findByPost(post: Post): List<CommentFacade> {
		var comments = mutableListOf<CommentFacade>()
		for(comment in commentRepository.findByPost(post)) {
			comments.add(toFacade(comment))
		}
		return comments
	}

	private fun toFacade(comment: Comment) : CommentFacade {
		return CommentFacade(comment.id,
							 comment.post!!.id,
							 comment.name,
							 comment.email,
							 comment.text,
							 comment.createdAt,
						 	 true)
	}

}