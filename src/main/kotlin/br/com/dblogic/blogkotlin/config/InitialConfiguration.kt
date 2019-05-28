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

@Configuration
class InitialConfiguration {
	
	private val logger = LoggerFactory.getLogger(InitialConfiguration::class.java)
	
	@Autowired
	lateinit var postService: PostService
	
	@Bean
	fun initDatabase() = CommandLineRunner {
		
		val lorem = LoremIpsum.getInstance()
		var titlemin = 0
		var titlemax = 0
		var paragraphmin = 0
		var paragraphmax = 0
				
		for(x in 0 until 10) {
			titlemin = ThreadLocalRandom.current().nextInt(1, 10)
			titlemax = ThreadLocalRandom.current().nextInt(titlemin, 12)
			paragraphmin = ThreadLocalRandom.current().nextInt(3, 12)
			paragraphmin = ThreadLocalRandom.current().nextInt(paragraphmin, 15)
			
			logger.info("Preloading: " + postService.save(Post(0,
														  lorem.getTitle(titlemin, titlemax),
														  lorem.getParagraphs(paragraphmin, paragraphmax))))
		}
	}
	
}