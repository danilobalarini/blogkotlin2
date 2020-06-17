package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("")
class BlogController {
	
	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var blogUtils: BlogUtils

	@Value("\${blog.directory.name}")
	lateinit var rootFolder: String

	@GetMapping("", "/home", "/index")
	fun goHome(model: Model) : String {
		model.addAttribute("frontPageFacade", postService.frontPage())
		return "index"
	}

	@GetMapping("/postid/{id}")
	fun goArticle(@PathVariable id: Long, model: Model): String {
		model.addAttribute("post", postService.goArticle(id))
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

	@GetMapping("/tags/{tag}")
	fun findByTag(@PathVariable tag: String, model: Model) : String {
		model.addAttribute("posts", postService.findByTag(tag))
		return "showbytag"
	}

	@GetMapping("/getall-posts")
	fun getAllPosts(@RequestParam(defaultValue = "0") pagenumber: Int,
					@RequestParam(defaultValue = "10") pageSize: Int,
					model: Model) : String {

		model.addAttribute("allposts", postService.getAllPosts(pagenumber, pageSize))

		return "allposts"
	}

}