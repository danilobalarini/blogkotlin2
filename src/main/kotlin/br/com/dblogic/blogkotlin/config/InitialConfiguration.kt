package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.model.Comment
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.User
import br.com.dblogic.blogkotlin.service.CommentService
import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.service.UserService
import br.com.dblogic.blogkotlin.utils.DateUtils
import com.thedeanda.lorem.LoremIpsum
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.ThreadLocalRandom

@Configuration
class InitialConfiguration {

	private final val DASH = "-"
	
	private val logger = LoggerFactory.getLogger(InitialConfiguration::class.java)
	
	@Autowired
	lateinit var postService: PostService
	
	@Autowired
	lateinit var commentService: CommentService

	@Autowired
	lateinit var userService: UserService
	
	@Autowired
	lateinit var dateUtils: DateUtils

	@Value("\${spring.jpa.hibernate.ddl-auto}")
	lateinit var ddlauto: String

	@Value("\${blog.directory.name}")
	lateinit var blogFolderName: String

	@Bean
	fun initDatabase() = CommandLineRunner {

		createPostDirectories()

		if(ddlauto.equals("create") || ddlauto.equals("create-drop")) {
			createEverything()
		} else {
			logger.info("opa e ae")
		}

		logger.info("Comment Count: " + commentService.count())
	}

	fun createEverything() {
		logger.info("creating all data")

		val maxPosts = 5
		val maxUsers = 10
		val users = createusers(maxUsers)
		
		val lorem = LoremIpsum.getInstance()
		
		for(x in 0 until maxPosts) {
			val titlemin = ThreadLocalRandom.current().nextInt(1, 10)
			val titlemax = ThreadLocalRandom.current().nextInt(titlemin, 12)
			val paragraphmin = ThreadLocalRandom.current().nextInt(3, 12)
			val paragraphmax = ThreadLocalRandom.current().nextInt(paragraphmin, 15)
			val datePost = dateUtils.getRandomDateSince(30)
			
			var post = Post(lorem.getTitle(titlemin, titlemax),
							lorem.getParagraphs(paragraphmin, paragraphmax),
							datePost)
			
			var plusInstant = dateUtils.plusInstantUntilNow(datePost)
			logger.info("plusInstant: " + dateUtils.toLocalDate(plusInstant)) 
			
			for(y in 0..ThreadLocalRandom.current().nextInt(0, 15)) {
			
				logger.info("Comment " + y + " - instant: " + dateUtils.toLocalDate(plusInstant))
				
				val comment = Comment("" + y + ": " + lorem.getTitle(2, 4),
									post,
									users.get(ThreadLocalRandom.current().nextInt(0, maxUsers)),
									plusInstant)
				
				post.addComment(comment)
				plusInstant = dateUtils.plusInstantUntilNow(plusInstant)
			}
			postService.save(post)
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

	fun createPostDirectories() {

		logger.info("entering createPostDirectories()")

		for(p in postService.findAll()) {

			logger.info("Criando o título")
			val title = titleToFile(p)
			logger.info("Título do post " + p.id.toString() + ": " + title)

			// criar o diretório
			val currentWorkingDir: Path = Paths.get("").toAbsolutePath()

			val dirpath = "$currentWorkingDir/$blogFolderName/$title"
			val path = Paths.get(dirpath)

			val isDirectory = Files.isDirectory(path)

			if(!isDirectory) {
				logger.info("creating directory $path")
				File("$path").mkdirs()
			}
		}
	}

	private fun titleToFile(p: Post): String {
		// always put the date in the last part
		// id-title-date
		// date=YYYYMMDD
		// this is documentation
		val titleWithoutAccents = StringUtils.stripAccents(p.title)
		var title = ""

		for(s in StringUtils.split(titleWithoutAccents, StringUtils.SPACE)) {
			title = title + s.replace("[^A-Za-z0-9]".toRegex(), StringUtils.EMPTY) + DASH
		}

		return p.id.toString() + DASH + title + toDateString(p.createdAt)
	}

	private fun toDateString(date: Instant) : String {

		val zone = ZoneOffset.UTC

		val year = LocalDateTime.ofInstant(date, zone).year.toString()
		val month = StringUtils.leftPad(LocalDateTime.ofInstant(date, zone).monthValue.toString(), 2, "0")
		val day = StringUtils.leftPad(LocalDateTime.ofInstant(date, zone).dayOfMonth.toString(), 2, "0")

		return "$year$month$day"
	}
	
}