package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.service.PostCoverImageService
import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import br.com.dblogic.blogkotlin.model.UploadForm

@RestController
@RequestMapping("/post")
class PostController {

	private val logger = LoggerFactory.getLogger(PostController::class.java)
	
	@Autowired
	lateinit var postService: PostService;

	@Autowired
	lateinit var postCoverImageService: PostCoverImageService;

	@GetMapping("/findAll")
	fun findAll(): List<Post> {
		return postService.findAll()
	}
	
	@GetMapping("/findById")
	fun findById(@RequestParam id: Long): Post {
		return findById(id)
	}

	@PostMapping("/update")
	fun update(@RequestBody post: Post): Post {
		logger.info("post.id: " + post.id)
		logger.info("post.title: " + post.title)
		logger.info("post.text: " + post.text)
		return postService.save(post)
	}

	@PostMapping("/updateCoverImage")
	@ResponseBody
	fun updateCoverImage(@ModelAttribute form: UploadForm) : ResponseEntity<String> {

		postCoverImageService.updateFrontPageCoverImage(form.id, form.coverImage[0])

		return ResponseEntity("alguma coisa", HttpStatus.OK)
	}

	@PostMapping("/save")
	fun save(@RequestBody post: Post): Post {
		logger.info("post.title: " + post.title)
		logger.info("post.text: " + post.text)
		return postService.save(post)
	}

}