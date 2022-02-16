package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.model.*
import br.com.dblogic.blogkotlin.service.*
import br.com.dblogic.blogkotlin.utils.BlogUtils
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
	lateinit var tagService: TagService

	@Autowired
	lateinit var blogUtils: BlogUtils

	@Value("\${spring.jpa.hibernate.ddl-auto}")
	lateinit var ddlauto: String

	@Bean
	fun init() = CommandLineRunner {
		if(ddlauto == "create" || ddlauto == "create-drop") {
			createTags()
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

}