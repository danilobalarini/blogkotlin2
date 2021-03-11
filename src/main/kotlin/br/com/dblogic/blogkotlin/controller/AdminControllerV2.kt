package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.service.TagService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/adminv2")
class AdminControllerV2 {

	private val logger = LoggerFactory.getLogger(AdminControllerV2::class.java)

	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var tagService: TagService

	@GetMapping("")
	fun admin(model: Model) : String {
		//model.addAttribute("posts", postService.returnAllFacades())
		logger.info("versão 2")
		return "admindex2"
	}

	@GetMapping("/tags")
	fun manageTags(model: Model) : String {
		logger.info("let's go manage tags")

		return "admindex2_tags"
	}

}