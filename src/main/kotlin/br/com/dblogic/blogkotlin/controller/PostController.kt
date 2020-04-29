package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.service.PostImageService
import br.com.dblogic.blogkotlin.service.PostService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
class PostController {

	private val logger = LoggerFactory.getLogger(PostController::class.java)
	
	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var postImageService: PostImageService

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
		logger.info("post.text: " + postFacade.text)
		return postService.updateComposer(postFacade)
	}

	@PostMapping("/save")
	fun save(@RequestBody post: Post): Post {
		logger.info("post.title: " + post.title)
		logger.info("post.text: " + post.text)
		return postService.save(post)
	}

}