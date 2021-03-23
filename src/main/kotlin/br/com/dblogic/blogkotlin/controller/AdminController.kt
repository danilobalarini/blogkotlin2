package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.service.TagService
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.Instant

@Controller
@RequestMapping("/admin")
class AdminController {

	private val logger = LoggerFactory.getLogger(AdminController::class.java)

	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var tagService: TagService

	@GetMapping("")
	fun admin(model: Model) : String {
		model.addAttribute("posts", postService.returnAllFacades())
		return "admindex"
	}

	@GetMapping("/tags")
	fun tags(model: Model) : String {
		model.addAttribute("tags", tagService.findAll())
		return "managetags"
	}

	@ResponseBody
	@GetMapping("/compose")
	fun compose(model : Model) : PostFacade {
		logger.info("chegou e vai criar um novo post")
		val post = postService.brandNewPost("")
		return PostFacade(post.id, "", "")
	}

	@GetMapping("/updatepost")
	fun updatepost(@RequestParam id: Long, model : Model) : String {

		val post = postService.findById(id)
		logger.info("post.review: >>>>>>>>> ${post.review}")
		val facade = PostFacade(id,
								post.title,
								StringUtils.replace(post.review, "\n", "<br/>"),
								post.isDraft,
								Instant.now(),
								0,
								tagService.toSetFacade(post.tags),
								"../${postService.createCoverImage(post)}")

		model.addAttribute("post", facade)
		model.addAttribute("alltags", HashSet(tagService.findAll()))
		model.addAttribute("idtags", tagService.onlyIds(post.tags))

		logger.info("onlyids: " + tagService.onlyIds(post.tags))

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