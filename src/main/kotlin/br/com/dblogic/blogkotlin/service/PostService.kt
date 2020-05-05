package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.FrontPageFacade
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.FrontPagePostFacade
import br.com.dblogic.blogkotlin.model.facade.PostAndCoverImageFacade
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.repository.CommentRepository
import br.com.dblogic.blogkotlin.repository.PostRepository
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
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
	lateinit var postImageService: PostImageService

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

	fun frontPage() : FrontPageFacade {
		logger.info("posts zero length? " + (postRepository.count() == 0L));
		val posts = cleanHtml(postRepository.findTop6ByOrderByCreatedAtDesc())
		logger.info("### posts ###: " + posts.size)

		val first = posts.first()
		val post = FrontPagePostFacade(first, commentRepository.countByPost(first), createCoverImage(first), first.createdAt)

		var listPostComments = mutableListOf<FrontPagePostFacade>()

		for (p in posts.drop(1)) {
			listPostComments.add(FrontPagePostFacade(p, commentRepository.countByPost(p), createCoverImage(p), p.createdAt))
		}

		return FrontPageFacade(post, listPostComments)
	}

	fun goArticle(id: Long): PostAndCoverImageFacade {
		val post = findById(id)
		return PostAndCoverImageFacade(post, createCoverImage(post))
	}

	fun allPosts(): List<FrontPagePostFacade> {
		val posts = postRepository.findAll()

		var listPostComments = mutableListOf<FrontPagePostFacade>()

		for (p in posts.drop(1)) {
			listPostComments.add(FrontPagePostFacade(p, commentRepository.countByPost(p), createCoverImage(p), p.createdAt))
		}

		return listPostComments

	}

	fun getAllPosts(pageNumber: Int = 0, pageSize: Int = 10): List<FrontPagePostFacade> {

		val paging = PageRequest.of(pageNumber, pageSize)
		val pagedResult = postRepository.findAllCustomBy(paging)

		val listPostComments = mutableListOf<FrontPagePostFacade>()
		if(pagedResult.isNotEmpty()) {
			for(p in pagedResult.toList()) {

				val coverImage = "${blogUtils.getDirectoryName(p.post.id, p.post.title, p.createdAt)}/${p.coverImage}"

				listPostComments.add(FrontPagePostFacade(Post(p.post.id, p.post.title, p.post.text),
														 p.commentCount,
														 coverImage,
														 p.createdAt))
			}
		}
		return listPostComments
	}

	fun createCoverImage(post: Post): String {
		val directoryName = blogUtils.getDirectoryNameFromPost(post)
		val imageName = postImageService.findCoverImage(post).filename

		return "$rootFolder/$directoryName/$imageName"
	}

	fun updateComposer(p: PostFacade): PostFacade {
		logger.info("updateComposer: ${p.id}")

		var post = findById(p.id)
		post.title = p.title
		post.text = p.text

		return postToFacade(postRepository.save(post))
	}

	fun createCoverImagePath(post: Post): Path {
		val directoryName = blogUtils.getDirectoryNameFromPost(post)
		val imageName = postImageService.findCoverImage(post).filename

		return Paths.get("$rootFolder/$directoryName/$imageName")
	}

	fun postToFacade(p: Post): PostFacade {
		return PostFacade(p.id,
				          p.title,
						  p.text)
	}

	fun facadeToPost(pf: PostFacade) : Post {
		return Post(pf.id,
					pf.title,
					pf.text)
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