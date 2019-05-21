package br.com.dblogic.blogkotlin.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("", "/blog")
class BlogController {
	
	@GetMapping("")
	fun goHome() : String {
		return "redirect:/index"
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