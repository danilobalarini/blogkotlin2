package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.service.PostService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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
	fun save(@RequestBody postFacade: PostFacade): ResponseEntity<Post> {
		logger.info("post.title: " + postFacade.title)
		return ResponseEntity(postService.brandNewPost(postFacade.title), HttpStatus.OK)
	}

	@PostMapping("/updatetags")
	fun updateTags(@RequestBody postFacade: PostFacade): ResponseEntity<String> {
		postService.updateTags(postFacade)

		return ResponseEntity("ok", HttpStatus.OK)
	}

	@GetMapping("/delete")
	fun delete(@RequestParam(defaultValue = "0") id: Long) {
		postService.deleteById(id)
	}

}