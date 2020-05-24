package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.FrontPageFacade
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostImage
import br.com.dblogic.blogkotlin.model.facade.FrontPagePostFacade
import br.com.dblogic.blogkotlin.model.facade.PostAndCoverImageFacade
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.repository.CommentRepository
import br.com.dblogic.blogkotlin.repository.PostRepository
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.time.Instant


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
		logger.info("posts length: ${postRepository.count()}");
		val posts = cleanHtml(postRepository.findTop6ByIsDraftFalseOrderByCreatedAtDesc())
		logger.info("### posts ###: " + posts.size)

		if(posts.isEmpty()) {

			val post = Post(0L, "Não temos posts. Volte outro dia. Teremos bolinhos.", "")

			val fppf = FrontPagePostFacade(post, 0L, "", Instant.now())
			return FrontPageFacade(fppf, mutableListOf<FrontPagePostFacade>())
		} else {
			val first = posts.first()
			val post = FrontPagePostFacade(first, commentRepository.countByPost(first), createCoverImage(first), first.createdAt)

			var listPostComments = mutableListOf<FrontPagePostFacade>()

			for (p in posts.drop(1)) {
				listPostComments.add(FrontPagePostFacade(p, commentRepository.countByPost(p), createCoverImage(p), p.createdAt))
			}

			return FrontPageFacade(post, listPostComments)
		}
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

	fun getAllPageable(pageable: Pageable): Page<FrontPagePostFacade> {

		val page = postRepository.findAllPageable(pageable)

		val facadeList = mutableListOf<FrontPagePostFacade>()
		for(f in page.content) {
			val dirname = blogUtils.getDirectoryName(f.post.id, f.post.title, f.createdAt)
			f.coverImage = "$dirname/${f.coverImage}"
			facadeList.add(f)
		}

		return PageImpl(facadeList, pageable, pageable.pageSize.toLong())
	}

	fun brandNewPost(): Post {
		var post = postRepository.save(Post())
		val postImage = PostImage("${post.id}-coverimage.jpg","default cover image", postImageService.defaultCoverImage(post.title), true, post)
		post.addPostImage(postImage)

		val directory = blogUtils.getDirectoryNameFromPost(post)
		logger.info("creating directory $directory")
		val dirpath = blogUtils.appendToBlogDir(blogUtils.getDirectoryNameFromPost(post))
		logger.info("creating dirpath $dirpath")
		File("$dirpath").mkdirs()

		logger.info("filename ${postImage.filename}")
		val filepath = Paths.get("$dirpath/${postImage.filename}")
		Files.write(filepath, postImage.image, StandardOpenOption.CREATE)

		return postRepository.save(post)
	}

	fun createCoverImage(post: Post): String {
		val directoryName = blogUtils.getDirectoryNameFromPost(post)
		val imageName = postImageService.findCoverImage(post).filename

		return "$rootFolder/$directoryName/$imageName"
	}

	fun updateComposer(p: PostFacade): PostFacade {
		logger.info("updateComposer: ${p.id}")

		var post = findById(p.id)

		if(!StringUtils.equals(post.title, p.title)) {
			logger.info("the title has been changed")

			val olddir = blogUtils.getDirectoryPathFromPost(post)
			logger.info("olddir $olddir")

			post.title = p.title
			val newdir = blogUtils.getDirectoryPathFromPost(post)
			logger.info("newdir $newdir")

			logger.info("renaming directory")
			Files.move(olddir, newdir, StandardCopyOption.REPLACE_EXISTING)

			val oldTitle = StringUtils.difference(newdir.toString(), olddir.toString())
			val newTitle = StringUtils.difference(olddir.toString(), newdir.toString())

			post.text = StringUtils.replace(post.text, oldTitle, newTitle)
		} else {
			post.text = p.text
		}

		post.isDraft = p.isDraft

		val savepost = postRepository.save(post)

		return postToFacade(savepost)
	}

	fun postToFacade(p: Post): PostFacade {
		return PostFacade(p.id,
				          p.title,
						  p.text,
		                  p.isDraft)
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