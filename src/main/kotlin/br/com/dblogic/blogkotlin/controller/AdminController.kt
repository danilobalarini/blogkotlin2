package br.com.dblogic.blogkotlin.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class AdminController {

	private val LOGGER = LoggerFactory.getLogger(AdminController::class.java)
	
	@GetMapping("")
	fun admin() : String {
		return "admin"
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