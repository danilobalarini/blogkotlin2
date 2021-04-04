package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.service.CommentService
import br.com.dblogic.blogkotlin.service.ContactService
import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.service.TagService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.Instant
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
@RequestMapping("/admin")
class AdminController {

	private val logger = LoggerFactory.getLogger(AdminController::class.java)

	@Autowired
	lateinit var postService: PostService

	@Autowired
	lateinit var tagService: TagService

	@Autowired
	lateinit var contactService: ContactService

	@Autowired
	lateinit var commentService: CommentService

	@GetMapping("")
	fun admin(model: Model) : String {
		logger.info("versão 2")
		return "admindex"
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

	@GetMapping("/updatepost")
	fun updatepost(@RequestParam id: Long, model : Model) : String {
		val post = postService.findById(id)
		logger.info("post.review: >>>>>>>>> ${post.review}")
		val facade = PostFacade(id,
								post.title,
								post.review.replace( "\n", "<br/>"),
								post.isDraft,
								Instant.now(),
								0,
								tagService.toSetFacade(post.tags),
								"../${postService.createCoverImage(post)}")

		val tags = HashSet(tagService.findByPostId(id))
		model.addAttribute("post", facade)
		model.addAttribute("postTagsOwned", tagService.tagsLeft(tags))
		model.addAttribute("postTags", tags)

		return "adm_compose"
	}

	@ModelAttribute("numberContactsFalse")
	private fun getContactsFalse(): Long {
		return contactService.countCheckedFalse()
	}

	@ModelAttribute("numberCommentsNotApproved")
	private fun getCommentsApprovedFalse(): Long {
		return commentService.countByApprovedTrue()
	}

}