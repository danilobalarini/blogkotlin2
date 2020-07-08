package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.CaptchaEvent
import br.com.dblogic.blogkotlin.model.facade.ContactFacade
import br.com.dblogic.blogkotlin.service.CaptchaService
import br.com.dblogic.blogkotlin.service.CommentService
import br.com.dblogic.blogkotlin.service.ContactService
import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("")
class BlogController {

	private val logger = LoggerFactory.getLogger(BlogController::class.java)
	
	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var captchaService: CaptchaService

	@Autowired
	lateinit var contactService: ContactService

	@Autowired
	lateinit var commentService: CommentService

	@Autowired
	lateinit var blogUtils: BlogUtils

	@Value("\${blog.directory.name}")
	lateinit var rootFolder: String

	@Value("\${google.recaptcha.key.site}")
	lateinit var recaptchaKeySite: String

	@GetMapping("", "/home", "/index")
	fun goHome(model: Model) : String {
		model.addAttribute("frontPageFacade", postService.frontPage())
		return "index"
	}

	@GetMapping("/postid/{id}")
	fun goArticle(@PathVariable id: Long, model: Model): String {
		val post = postService.findById(id)
		model.addAttribute("post", postService.postToFacade(post))
		model.addAttribute("comments", commentService.findByPost(post))
		model.addAttribute("keysite", recaptchaKeySite)
		model.addAttribute("commentAction", CaptchaEvent.COMMENT)
		return "article"
	}

	@GetMapping("/about")
	fun about() : String {
		return "about"
	}

	@GetMapping("/contact")
	fun contact(model: Model) : String {
		logger.debug("keysite: $recaptchaKeySite")
		logger.debug("registerAction: ${CaptchaEvent.CONTACT}")
		model.addAttribute("keysite", recaptchaKeySite)
		model.addAttribute("registerAction", CaptchaEvent.CONTACT)
		return "contact"
	}

	@PostMapping("/getContact")
	fun getContact(@RequestBody contactFacade: ContactFacade, model: Model): String {
		logger.info("!!!! chegou no getContact !!!!")
		logger.info("nome:  ${contactFacade.name}")
		logger.info("email: ${contactFacade.email}")
		logger.info("mensagem: ${contactFacade.message}")
		logger.info("response: ${contactFacade.response}")

		contactService.save(contactFacade)

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