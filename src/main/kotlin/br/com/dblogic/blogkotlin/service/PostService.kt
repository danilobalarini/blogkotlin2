package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.FrontPageFacade
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.PostCommentCountFacade
import br.com.dblogic.blogkotlin.repository.CommentRepository
import br.com.dblogic.blogkotlin.repository.PostRepository
import br.com.dblogic.blogkotlin.repository.specification.PostSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class PostService {
	
	@Autowired
	lateinit var postRepository: PostRepository
	
	@Autowired
	lateinit var commentRepository: CommentRepository
	
	@Autowired
	lateinit var postSpecification: PostSpecification
	
	fun findAll(): List<Post> {
		return postRepository.findAll()
	}

	fun findById(id: Long) : Post {

		val post = postRepository.findById(id).get()
		
		return post
	}
	
	fun save(post: Post) : Post {
		return postRepository.save(post)
	}

	fun deleteById(id: Long) {
		postRepository.deleteById(id)
	}
	
	fun frontPage2() : List<Post> {
		val posts = postSpecification.frontPage2()
		return postRepository.findAll(posts)
	}
	
	fun frontPage() : FrontPageFacade {
		
		val posts = postRepository.findTop6ByOrderByCreatedAtDesc()
		val post = PostCommentCountFacade(posts.first(), commentRepository.countByPost(posts.first()))
		
		var listPostComments = mutableListOf<PostCommentCountFacade>()
		
		for(p in posts.drop(1)) {
			listPostComments.add(PostCommentCountFacade(p, commentRepository.countByPost(p)))
		}
		
		val facade = FrontPageFacade(post, listPostComments)
		
		return facade
	}
	
}