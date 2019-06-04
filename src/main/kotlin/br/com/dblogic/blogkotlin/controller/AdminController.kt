package br.com.dblogic.blogkotlin.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class AdminController {
	
	@GetMapping("")
	fun admin(model: Model) : String {
		
		//model.addAttribute("facade", postService.frontPage())
		
		return "admin"
	}

	
}