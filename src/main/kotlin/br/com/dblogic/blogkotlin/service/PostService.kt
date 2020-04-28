package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.FrontPageFacade
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.FrontPagePostFacade
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.repository.CommentRepository
import br.com.dblogic.blogkotlin.repository.PostRepository
import br.com.dblogic.blogkotlin.repository.specification.PostSpecification
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.nio.file.Paths

@Service
class PostService {

	private val logger = LoggerFactory.getLogger(PostService::class.java)
	
	@Autowired
	lateinit var postRepository: PostRepository
	
	@Autowired
	lateinit var commentRepository: CommentRepository
	
	@Autowired
	lateinit var postSpecification: PostSpecification

	@Autowired
	lateinit var postCoverImageService: PostCoverImageService

	@Autowired
	lateinit var blogUtils: BlogUtils

	@Value("\${blog.directory.name}")
	lateinit var rootFolder: String
	
	fun findAll(): List<Post> {
		return postRepository.findAll()
	}

	fun findById(id: Long) : Post {
		return postRepository.findById(id).get()
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
		logger.info("posts zero length? " + (postRepository.count() == 0L));
		val posts = cleanHtml(postRepository.findByOrderByCreatedAtDesc())
		logger.info("### posts ###: " + posts.size)

		val first = posts.first()
		val post = FrontPagePostFacade(first, commentRepository.countByPost(first), createCoverImage(first))

		var listPostComments = mutableListOf<FrontPagePostFacade>()

		for (p in posts.drop(1)) {
			val ci = postCoverImageService.findByPost(p)
			listPostComments.add(FrontPagePostFacade(p, commentRepository.countByPost(p), createCoverImage(p)))
		}

		return FrontPageFacade(post, listPostComments)
	}

	fun goArticle(id: Long): PostFacade {
		val post = findById(id)
		return PostFacade(post, createCoverImage(post))
	}

	fun createCoverImage(post: Post): String {
		val directoryName = blogUtils.getDirectoryNameFromPost(post)
		val imageName = postCoverImageService.findByPost(post).filename

		return "$rootFolder/$directoryName/$imageName"
	}

	fun createCoverImagePath(post: Post): Path {
		val directoryName = blogUtils.getDirectoryNameFromPost(post)
		val imageName = postCoverImageService.findByPost(post).filename

		return Paths.get("$rootFolder/$directoryName/$imageName")
	}

	private fun cleanHtml(posts: List<Post>) : List<Post> {

		var listPost = mutableListOf<Post>()
		for(p in posts) {
			// logger.info("antes: " + p.text)
			// logger.info("depois: " + p.text.toString().replace("\\<.*?>".toRegex(),""))
			p.text = p.text.replace("</p><p>", " ")
			p.text = p.text.replace("<br/>", " ")
			p.text = p.text.replace("<br>", " ")
			p.text = p.text.replace("\\<.*?>".toRegex(),"")
			listPost.add(p)
		}
		return posts;
	}

}