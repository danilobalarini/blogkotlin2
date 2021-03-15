package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Tag
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
	fun listtags(model: Model) : String {
		logger.info("show all tags")

		model.addAttribute("alltags", tagService.findAll())
		return "adm_tags_list"
	}

	@GetMapping("/tags_update")
	fun edittags(@RequestParam(defaultValue = "0") id: Long, model: Model) : String {
		logger.info("Let's go manage tags")

		model.addAttribute("tag", if(id==0L) Tag() else tagService.findById(id))
		return "adm_tags_edit"
	}

	@GetMapping("/posts")
	fun listposts(model: Model) : String {
		logger.info("show all posts")

		model.addAttribute("allposts", postService.findAll())
		return "adm_posts_list"
	}

}