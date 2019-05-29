package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.service.PostService
import com.thedeanda.lorem.LoremIpsum
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ThreadLocalRandom
import br.com.dblogic.blogkotlin.repository.DateAuditRepository

@Configuration
class InitialConfiguration {
	
	private val logger = LoggerFactory.getLogger(InitialConfiguration::class.java)
	
	@Autowired
	lateinit var postService: PostService
	
	@Bean
	fun initDatabase() = CommandLineRunner {
		
		val lorem = LoremIpsum.getInstance()
		
		for(x in 0 until 10) {
			val titlemin = ThreadLocalRandom.current().nextInt(1, 10)
			val titlemax = ThreadLocalRandom.current().nextInt(titlemin, 12)
			val paragraphmin = ThreadLocalRandom.current().nextInt(3, 12)
			val paragraphmax = ThreadLocalRandom.current().nextInt(paragraphmin, 15)
			
			logger.info("Preloading: " + postService.save(Post(lorem.getTitle(titlemin, titlemax),
														  	   lorem.getParagraphs(paragraphmin, paragraphmax))))
			// sleep 1 second
			Thread.sleep(1_000)
		}
		
		val facade = postService.frontPage()
		logger.info("Count: " + facade.posts.size)
		
		/*for(post in posts) {
			logger.info(" ### Printing: " + post)
		}*/
		
	}
	
}