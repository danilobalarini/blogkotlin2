package br.com.dblogic.blogkotlin.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.beans.factory.annotation.Autowired

import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.model.Post
import org.apache.commons.lang3.StringUtils

@Controller
@RequestMapping("/admin")
class AdminController {

	private val logger = LoggerFactory.getLogger(AdminController::class.java)

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
	fun compose(model : Model) : String {

		model.addAttribute("post", Post())
		model.addAttribute("postHTML", "")

		return "compose"
	}

	@GetMapping("/updatepost")
	fun updatepost(post : Post, model : Model) : String {

		model.addAttribute("post", postService.save(post))

		return "compose"
	}

	@PostMapping("/update")
	fun update(post : Post, model : Model) {
		model.addAttribute("post", postService.save(post))
	}
	
}