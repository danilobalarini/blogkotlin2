package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.service.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/post")
class PostController {
	
	@Autowired
	lateinit var postService: PostService;
	
	@GetMapping("/findAll")
	fun findAll(): List<Post> {
		return postService.findAll();
	}
	
	@PostMapping("/create")
	fun save(@RequestBody post: Post): Post {
		return postService.save(post);
	}
	
}