package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.service.PostService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/post")
class PostController {

	private val logger = LoggerFactory.getLogger(PostController::class.java)
	
	@Autowired
	lateinit var postService: PostService

	@GetMapping("/findAll")
	fun findAll(): List<Post> {
		return postService.findAll()
	}

	@PostMapping("/findByTitleAndReview")
	fun findByTitleAndReview(@RequestBody post: Post): List<PostFacade> {
		logger.info("post.title: ${post.title}")
		logger.info("post.review: ${post.review}")
		return postService.findByTitleOrReviewOrderByCreatedAt(post)
	}
	
	@GetMapping("/findById")
	fun findById(@RequestParam id: Long): Post {
		return findById(id)
	}

	@PostMapping("/update")
	fun update(@RequestBody postFacade: PostFacade): PostFacade {
		logger.info("post.id: " + postFacade.id)
		logger.info("post.title: " + postFacade.title)
		logger.info("post.review: " + postFacade.review)
		return postService.updateComposer(postFacade)
	}

	@PostMapping("/save")
	fun save(@RequestBody post: Post): Post {
		logger.info("post.title: " + post.title)
		logger.info("post.review: " + post.review)
		return postService.save(post)
	}

	@PostMapping("/updatetags")
	fun updateTags(@RequestBody postFacade: PostFacade): ResponseEntity<String> {
		postService.updateTags(postFacade)

		return ResponseEntity("ok", HttpStatus.OK)
	}

}