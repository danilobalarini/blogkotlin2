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
		return "admindex"
	}

	@GetMapping("/compose")
	fun compose(model : Model) : String {

		val post = postService.save(Post())
		model.addAttribute("post", post)
		model.addAttribute("postHTML", "")

		return "compose"
	}

	@GetMapping("/updatepost")
	fun updatepost(id: Long, model : Model) : String {

		val post = postService.findById(id)
		post.text = StringUtils.replace(post.text, "\n", "<br/>")
		model.addAttribute("post", post)

		return "compose"
	}

	@PostMapping("/update")
	fun update(post : Post, model : Model) {
		model.addAttribute("post", postService.save(post))
	}

	@GetMapping("/deletepost")
	fun delete(@RequestParam id: Long, model: Model): String {
		postService.deleteById(id)
		model.addAttribute("posts", postService.findAll())
		return "admindex"
	}
	
}