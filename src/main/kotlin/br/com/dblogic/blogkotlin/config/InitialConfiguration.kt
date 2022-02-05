package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.model.*
import br.com.dblogic.blogkotlin.service.*
import br.com.dblogic.blogkotlin.utils.BlogUtils
import br.com.dblogic.blogkotlin.utils.DateUtils
import com.thedeanda.lorem.LoremIpsum
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.FileSystemUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@Configuration
class InitialConfiguration {

	private val logger = LoggerFactory.getLogger(InitialConfiguration::class.java)
	
	@Autowired
	lateinit var postService: PostService
	
	@Autowired
	lateinit var commentService: CommentService

	@Autowired
	lateinit var postImageService: PostImageService

	@Autowired
	lateinit var languageService: LanguageService

	@Autowired
	lateinit var tagService: TagService

	@Autowired
	lateinit var dateUtils: DateUtils

	@Autowired
	lateinit var blogUtils: BlogUtils

	@Value("\${spring.jpa.hibernate.ddl-auto}")
	lateinit var ddlauto: String

	@Bean
	fun init() = CommandLineRunner {
		if(ddlauto == "create" || ddlauto == "create-drop") {
			createTags()
			createLanguages()
			createEverything()
		} else {
			logger.info("opa e ae")
		}
		clearBlogDirectory()
		checkPosts()
		logger.info("Comment Count: " + commentService.count())
	}

	private fun createTags() {
		val tags = mutableListOf<Tag>(Tag("Java"),
								 	  Tag("Kotlin"),
									  Tag("Linux"),
									  Tag("ASP"),
									  Tag("Turbo Pascal"),
									  Tag("Visual Basic"),
									  Tag("Oracle"),
  									  Tag("SQL Server"),
									  Tag("PostgreSQL"),
									  Tag("Apache Kafka"),
									  Tag("Go"),
									  Tag("Flutter"))
		for(tag in tags) {
			tagService.save(tag)
		}
	}

	private fun clearBlogDirectory() {
		FileSystemUtils.deleteRecursively(blogUtils.getBlogDir())
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

				for(pi in postImageService.findByPost(p)) {
					val filepath = Paths.get("$path/${pi.filename}")
					Files.write(filepath, pi.image, StandardOpenOption.CREATE)
				}
			}
		}
	}

	fun createEverything() {
		logger.info("creating all data")

		val maxPosts = 5
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
							datePost,
							languageService.findById(1L))
			
			var plusInstant = dateUtils.plusInstantUntilNow(datePost)
			logger.info("plusInstant: " + dateUtils.toLocalDate(plusInstant))

			var plusInstantComment = plusInstant
			for(y in 0..ThreadLocalRandom.current().nextInt(0, 15)) {

				logger.info("Comment " + y + " - instant: " + dateUtils.toLocalDate(plusInstantComment))
				
				val comment = Comment(post,
									  "" + y + ": " + lorem.getTitle(2, 4),
									  "" + y + ": " + lorem.getTitle(2, 4),
									  "" + y + ": " + lorem.getTitle(2, 4),
									  false,
						 			  plusInstantComment)

				post.addComment(comment)
				plusInstantComment = dateUtils.plusInstantUntilNow(plusInstantComment)
			}

			logger.info("Creating file ${x+1}-image.jpg for cover image")

			val postImage = PostImage("${x+1}-coverimage.jpg", "", blogUtils.defaultCoverImage("${x+1}: $title"), true, post)
			post.addPostImage(postImage)

			postService.save(post)
		}
	}

	private fun createLanguages() {
		var portuguese = Language()
		portuguese.description = "Português Brasileiro"
		portuguese.locale = Locale("pt", "BR")
		portuguese = languageService.save(portuguese)

		logger.info("new language: ${portuguese.description}")

		var english = Language()
		english.description = "American English"
		english.locale = Locale("en", "US")
		english = languageService.save(english)

		logger.info("new language: ${english.description}")
	}

}