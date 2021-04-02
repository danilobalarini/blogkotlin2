package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.CaptchaEvent
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.model.facade.PostSearchFacade
import br.com.dblogic.blogkotlin.service.BlogService
import br.com.dblogic.blogkotlin.service.CommentService
import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("")
class BlogController {

	private val logger = LoggerFactory.getLogger(BlogController::class.java)
	
	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var blogService: BlogService

	@Autowired
	lateinit var commentService: CommentService

	@Autowired
	lateinit var blogUtils: BlogUtils

	@Value("\${google.recaptcha.key.site}")
	lateinit var recaptchaKeySite: String

	@GetMapping("", "/home", "/index")
	fun goHome(model: Model) : String {
		model.addAttribute("frontPageFacade", postService.frontPage())
		model.addAttribute("mostvisited", postService.mostVisitedPosts())

		return "index"
	}

	@GetMapping("/postid/{id}")
	fun goArticle(@PathVariable id: Long, @CookieValue(value = "postsvisited", defaultValue = "") postsvisited: String, model: Model, response: HttpServletResponse): String {
		logger.info("Posts visited: $postsvisited")

		val post = postService.findById(id)
		model.addAttribute("post", postService.postToFacade(post))
		model.addAttribute("comments", commentService.findByPostAndApprovedTrue(post))
		model.addAttribute("keysite", recaptchaKeySite)
		model.addAttribute("commentAction", CaptchaEvent.COMMENT)
		model.addAttribute("mostvisited", postService.mostVisitedPosts())

		response.addCookie(blogService.managePageViewCookie(postsvisited, post))

		return "article"
	}

	@GetMapping("/about")
	fun about(model: Model) : String {
		model.addAttribute("mostvisited", postService.mostVisitedPosts())
		return "about"
	}

	@GetMapping("/contact")
	fun contact(model: Model) : String {
		logger.debug("keysite: $recaptchaKeySite")
		logger.debug("registerAction: ${CaptchaEvent.CONTACT}")
		model.addAttribute("keysite", recaptchaKeySite)
		model.addAttribute("registerAction", CaptchaEvent.CONTACT)
		model.addAttribute("mostvisited", postService.mostVisitedPosts())
		return "contact"
	}

	@GetMapping("/all-posts")
	fun allPosts(model: Model) : String {

		val searchFacade = postService.findAllCreatedAt(PostSearchFacade())
		model.addAttribute("postSearchFacade", blogUtils.postSearchFacadeWithoutList(searchFacade))
		model.addAttribute("posts", searchFacade.posts)

		return "allposts"
	}

	@PostMapping("/all-posts")
	fun allPostsPagination(postSearchFacade: PostSearchFacade, model: Model) : String {

		val searchFacade = postService.findAllCreatedAt(postSearchFacade)
		model.addAttribute("postSearchFacade", blogUtils.postSearchFacadeWithoutList(searchFacade))
		model.addAttribute("posts", searchFacade.posts)

		return "allposts"
	}

	@GetMapping("/tags/{tag}")
	fun findByTag(@PathVariable tag: String, model: Model) : String {
		model.addAttribute("posts", postService.findByTag(tag))
		model.addAttribute("mostvisited", postService.mostVisitedPosts())
		return "showbytag"
	}

	@GetMapping("/archives")
	fun goArchives(model: Model) : String {
		model.addAttribute("keysite", recaptchaKeySite)
		model.addAttribute("registerAction", CaptchaEvent.SEARCH)
		model.addAttribute("postSearchFacade", PostSearchFacade())
		model.addAttribute("posts", mutableListOf<PostFacade>())

		return "archives"
	}

	@PostMapping("/searcharchives")
	fun searchArchives(postSearchFacade: PostSearchFacade,
					   model: Model) : String {

		val searchFacade = postService.findByTitleOrReviewOrderByCreatedAt(postSearchFacade)
		model.addAttribute("keysite", recaptchaKeySite)
		model.addAttribute("registerAction", CaptchaEvent.SEARCH)
		model.addAttribute("postSearchFacade", blogUtils.postSearchFacadeWithoutList(searchFacade))
		model.addAttribute("posts", searchFacade.posts)

		return "archives"
	}

}