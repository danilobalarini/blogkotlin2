package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.CaptchaEvent
import br.com.dblogic.blogkotlin.model.Comment
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.CommentFacade
import br.com.dblogic.blogkotlin.model.facade.CommentFormFacade
import br.com.dblogic.blogkotlin.repository.CommentRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class CommentService {

	private val logger = LoggerFactory.getLogger(CommentService::class.java)
	
	@Autowired
	lateinit var commentRepository: CommentRepository

	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var captchaService: CaptchaService

	fun count(): Long {
		return commentRepository.count()
	}

	fun countByApprovedTrue() : Long {
		return commentRepository.countByApprovedFalse()
	}
	
	fun findAll(): List<Comment> {
		return commentRepository.findAll()
	}
	
	fun save(comment: Comment) : Comment {
		return commentRepository.save(comment)
	}

	fun save(facade: CommentFormFacade): CommentFacade {

		logger.info("saving comment")
		val c = Comment(postService.findById(facade.post),
					    facade.name,
						facade.email,
						facade.text,
						false)
		val comment = commentRepository.save(c)

		logger.info("saving captcha response of contact")
		captchaService.save(facade.response, CaptchaEvent.COMMENT, comment.id)

		return toFacade(comment)
	}

	fun findByPostAndApprovedTrue(post: Post): List<CommentFacade> {
		return commentRepository.findByPostAndApprovedTrue(post)
				.stream()
				.map { c -> toFacade(c) }
				.collect(Collectors.toList())
	}

	fun toFacade(comment: Comment) : CommentFacade {
		return CommentFacade(comment.id,
							 comment.post.id,
							 comment.name,
							 comment.email,
							 comment.text,
							 comment.createdAt,
						 	 true)
	}

}