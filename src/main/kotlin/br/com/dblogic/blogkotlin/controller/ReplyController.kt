package br.com.dblogic.blog.controller

import br.com.dblogic.blog.model.Post
import br.com.dblogic.blog.model.Reply
import br.com.dblogic.blog.service.ReplyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reply")
class ReplyController {

	@Autowired
	lateinit var replyService: ReplyService

	@PostMapping("/addReply")
	fun add(@RequestBody reply: Reply): Reply {
		return replyService.save(reply)
	}
	
	@PostMapping("/findByPost")
	fun findByPost(@RequestBody post: Post): List<Reply> {
		return replyService.findByPost(post)
	}
	
}