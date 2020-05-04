package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.service.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("")
class BlogController {
	
	@Autowired
	lateinit var postService: PostService
	
	@GetMapping("", "/home", "/index")
	fun goHome(model: Model) : String {
		model.addAttribute("frontPageFacade", postService.frontPage())
		return "index"
	}

	@GetMapping("/postid/{id}")
	fun goArticle(@PathVariable id: Long, model: Model): String {
		model.addAttribute("postFacade", postService.goArticle(id))
		return "article"
	}

	@GetMapping("/about")
	fun about() : String {
		return "about"
	}

	@GetMapping("/contact")
	fun contact() : String {
		return "contact"
	}

	@GetMapping("/all-posts")
	fun allPosts(model: Model) : String {
		model.addAttribute("allposts", postService.allPosts())
		return "allposts"
	}

}