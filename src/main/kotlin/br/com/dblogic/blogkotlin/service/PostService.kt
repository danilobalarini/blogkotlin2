package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.FrontPageFacade
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.repository.PostRepository
import br.com.dblogic.blogkotlin.repository.specification.PostSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostService {
	
	@Autowired
	lateinit var postRepository: PostRepository
	
	@Autowired
	lateinit var postSpecification: PostSpecification
	
	fun findAll(): List<Post> {
		return postRepository.findAll()
	}
	
	fun save(post: Post) : Post {
		return postRepository.save(post)
	}
	
	fun frontPage2() : List<Post> {
		val posts = postSpecification.frontPage2()
		return postRepository.findAll(posts)
	}
	
	fun frontPage() : FrontPageFacade {
		
		val posts = postRepository.findTop6ByOrderByCreatedAtDesc()
		val facade = FrontPageFacade(posts.first(), posts.drop(1))
		
		return facade
	}
	
}