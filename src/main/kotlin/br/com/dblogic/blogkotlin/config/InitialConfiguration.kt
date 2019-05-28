package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.service.PostService
import com.thedeanda.lorem.LoremIpsum
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InitialConfiguration {
	
	private val logger = LoggerFactory.getLogger(InitialConfiguration::class.java)
	
	@Autowired
	lateinit var postService: PostService
	
	@Bean
	fun initDatabase() = CommandLineRunner {
		
		val lorem = LoremIpsum.getInstance()
		
		logger.info("Preloading: " + postService.save(Post(0, lorem.getTitle(3, 6), lorem.getParagraphs(4, 8))))
		logger.info("Preloading: " + postService.save(Post(0, lorem.getTitle(4, 7), lorem.getParagraphs(6, 9))))
		logger.info("Preloading: " + postService.save(Post(0, lorem.getTitle(3, 6), lorem.getParagraphs(4, 8))))
		logger.info("Preloading: " + postService.save(Post(0, lorem.getTitle(4, 7), lorem.getParagraphs(6, 9))))
		logger.info("Preloading: " + postService.save(Post(0, lorem.getTitle(3, 6), lorem.getParagraphs(4, 8))))
		logger.info("Preloading: " + postService.save(Post(0, lorem.getTitle(4, 7), lorem.getParagraphs(6, 9))))
				
	}

	
}