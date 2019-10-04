package br.com.dblogic.blogkotlin.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import br.com.dblogic.blogkotlin.service.PostService

@Controller
@RequestMapping("/admin")
class AdminController {

	private val LOGGER = LoggerFactory.getLogger(AdminController::class.java)

	@Autowired
	lateinit var postService: PostService
	
	@GetMapping("")
	fun admin(model: Model) : String {

		model.addAttribute("posts", postService.findAll())

		return "admindex"
	}

	@GetMapping("/reports")
	fun reports() : String {
		return "admin"
	}

	@GetMapping("/compose")
	fun compose() : String {
		return "compose"
	}

	@PostMapping("/savecompose")
	fun savecompose() {
		LOGGER.info("salvou!!!")
	}
	
}