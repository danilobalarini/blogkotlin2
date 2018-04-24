package br.com.dblogic.blog.service

import org.springframework.stereotype.Service
import br.com.dblogic.blog.model.Reply
import br.com.dblogic.blog.model.Post
import org.springframework.beans.factory.annotation.Autowired
import br.com.dblogic.blog.repository.ReplyRepository

@Service
class ReplyService {
	
	@Autowired
	lateinit var replyRepository: ReplyRepository
	
	fun save(reply: Reply) : Reply {
		return replyRepository.save(reply)
	}
	
	fun findByPost(post: Post) : List<Reply> {
		return replyRepository.findByPost(post)
	}
	
}