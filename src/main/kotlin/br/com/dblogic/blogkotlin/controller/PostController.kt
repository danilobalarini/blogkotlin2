package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.service.PostService
import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/post")
class PostController {

	private val log = LoggerFactory.getLogger(PostController::class.java)
	
	@Autowired
	lateinit var postService: PostService;
	
	@GetMapping("/findAll")
	fun findAll(): List<Post> {
		return postService.findAll();
	}
	
	@PostMapping("/create")
	fun save(@RequestBody post: Post): String {
		log.info("creating...")

		postService.save(post)

		return "compose"
	}

	@GetMapping("/article")
	fun article(model: Model) : String {
		
		log.info("article creating ")
		model.addAttribute("article"
		)
		log.info("article exiting ")

		return "article"
	}

}