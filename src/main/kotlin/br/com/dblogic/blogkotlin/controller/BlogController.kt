package br.com.dblogic.blogkotlin.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import br.com.dblogic.blogkotlin.service.PostService

@Controller
@RequestMapping("")
class BlogController {
	
	@Autowired
	lateinit var postService: PostService
	
	@GetMapping("", "/home", "/index")
	fun goHome(model: Model) : String {
		
		model.addAttribute("facade", postService.frontPage())
		
		return "index"
	}
	
	@GetMapping("/index")
	fun index() : String {
		return "index"
	}

	@GetMapping("/about")
	fun about() : String {
		return "about"
	}	

	@GetMapping("/contact")
	fun contact() : String {
		return "contact"
	}	
		
}