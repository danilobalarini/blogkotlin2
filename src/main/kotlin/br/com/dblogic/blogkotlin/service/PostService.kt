package br.com.dblogic.blog.service

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.repository.PostRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostService {
	
	@Autowired
	lateinit var postRepository: PostRepository;
	
	fun findAll(): List<Post> {
		return postRepository.findAll();
	}
	
	fun save(post: Post) : Post {
		return postRepository.save(post);
	}
	
}