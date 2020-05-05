package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.model.Comment
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostImage
import br.com.dblogic.blogkotlin.model.User
import br.com.dblogic.blogkotlin.service.CommentService
import br.com.dblogic.blogkotlin.service.PostImageService
import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.service.UserService
import br.com.dblogic.blogkotlin.utils.BlogUtils
import br.com.dblogic.blogkotlin.utils.DateUtils
import com.thedeanda.lorem.LoremIpsum
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.ThreadLocalRandom

@Configuration
class InitialConfiguration {

	private val logger = LoggerFactory.getLogger(InitialConfiguration::class.java)
	
	@Autowired
	lateinit var postService: PostService
	
	@Autowired
	lateinit var commentService: CommentService

	@Autowired
	lateinit var userService: UserService

	@Autowired
	lateinit var postImageService: PostImageService

	@Autowired
	lateinit var dateUtils: DateUtils

	@Autowired
	lateinit var blogUtils: BlogUtils

	@Value("\${spring.jpa.hibernate.ddl-auto}")
	lateinit var ddlauto: String

	@Bean
	fun initDatabase() = CommandLineRunner {

		if(ddlauto.equals("create") || ddlauto.equals("create-drop")) {
			createEverything()
		} else {
			logger.info("opa e ae")
		}

		checkPosts()

		logger.info("Comment Count: " + commentService.count())
	}

	fun checkPosts() {
		logger.info("entering createPostDirectories()")

		for(p in postService.findAll()) {

			logger.info("Criando o título")
			val title = blogUtils.getDirectoryNameFromPost(p)
			logger.info("Título do post: $title")

			// criação/verificação se existe o dir
			val dirpath = blogUtils.appendToBlogDir(title)
			val path = Paths.get(dirpath)

			val isDirectory = Files.isDirectory(path)

			if(!isDirectory) {
				logger.info("creating directory $path")
				File("$path").mkdirs()

				val postImage = postImageService.findCoverImage(p)
				val name = postImage.filename
				val filepath = Paths.get("$path/$name")

				Files.write(filepath, postImage.image, StandardOpenOption.CREATE)
			}
		}
	}

	fun createEverything() {

		logger.info("creating all data")

		val maxPosts = 150
		val maxUsers = 12
		val users = createusers(maxUsers)
		
		val lorem = LoremIpsum.getInstance()
		
		for(x in 0 until maxPosts) {
			val titlemin = ThreadLocalRandom.current().nextInt(1, 10)
			val titlemax = ThreadLocalRandom.current().nextInt(titlemin, 12)
			val paragraphmin = ThreadLocalRandom.current().nextInt(3, 12)
			val paragraphmax = ThreadLocalRandom.current().nextInt(paragraphmin, 15)
			val datePost = dateUtils.getRandomDateSince(30)

			val title = lorem.getTitle(titlemin, titlemax)
			var post = Post(title,
							lorem.getParagraphs(paragraphmin, paragraphmax),
							datePost)
			
			var plusInstant = dateUtils.plusInstantUntilNow(datePost)
			logger.info("plusInstant: " + dateUtils.toLocalDate(plusInstant))

			var plusInstantComment = plusInstant
			for(y in 0..ThreadLocalRandom.current().nextInt(0, 15)) {

				logger.info("Comment " + y + " - instant: " + dateUtils.toLocalDate(plusInstantComment))
				
				val comment = Comment("" + y + ": " + lorem.getTitle(2, 4),
									post,
									users.get(ThreadLocalRandom.current().nextInt(0, maxUsers)),
						 			plusInstantComment)
				
				post.addComment(comment)
				plusInstantComment = dateUtils.plusInstantUntilNow(plusInstantComment)
			}

			logger.info("Creating file ${x+1}-image.jpg for cover image")

			val postImage = PostImage("${x+1}-coverimage.jpg", "", postImageService.defaultCoverImage("${x+1}: $title"), true, post)
			post.addPostImage(postImage)

			val p = postService.save(post)

		}
	}
	
	fun createusers(max: Int): List<User> {
		
		val lorem = LoremIpsum.getInstance()
		var listUsers = mutableListOf<User>()
		
		for(x in 1..max) {
			val user = User(x, lorem.getTitle(1, 3))
			listUsers.add(userService.save(user))
		}
		return listUsers
	}

}