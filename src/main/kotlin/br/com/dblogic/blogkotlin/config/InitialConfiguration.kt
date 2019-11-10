package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.model.Comment
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.User
import br.com.dblogic.blogkotlin.service.CommentService
import br.com.dblogic.blogkotlin.service.PostService
import br.com.dblogic.blogkotlin.service.UserService
import com.thedeanda.lorem.LoremIpsum
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.util.concurrent.ThreadLocalRandom
import br.com.dblogic.blogkotlin.utils.DateUtils
import org.springframework.beans.factory.annotation.Value

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
	lateinit var dateUtils: DateUtils

	@Value("\${spring.jpa.hibernate.ddl-auto}")
	lateinit var ddlauto: String
	
	@Bean
	fun initDatabase() = CommandLineRunner {

		if(ddlauto.equals("create") || ddlauto.equals("create-drop")) {

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
		logger.info("Comment Count: " + commentService.count())
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