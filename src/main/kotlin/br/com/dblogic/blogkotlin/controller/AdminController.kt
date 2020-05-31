package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.service.PostService
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/admin")
class AdminController {

	private val logger = LoggerFactory.getLogger(AdminController::class.java)

	@Autowired
	lateinit var postService: PostService

	@GetMapping("")
	fun admin(model: Model) : String {

		model.addAttribute("posts", postService.returnAllFacades())

		return "admindex"
	}

	@GetMapping("/reports")
	fun reports() : String {
		return "admindex"
	}

	@ResponseBody
	@GetMapping("/compose")
	fun compose(model : Model) : PostFacade {
		logger.info("chegou e vai criar um novo post")
		val post = postService.brandNewPost()
		return PostFacade(post.id, "", "")
	}

	@GetMapping("/updatepost")
	fun updatepost(@RequestParam id: Long, model : Model) : String {

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