package br.com.dblogic.blogkotlin

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@SpringBootApplication
class BlogkotlinApplication : WebMvcConfigurer {
	
	private val log = LoggerFactory.getLogger(BlogkotlinApplication::class.java)
	
	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
	
		log.info(" ####### Entering ResourceHandlers configurations ####### ")
		
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
		registry.addResourceHandler("/javascript/**").addResourceLocations("classpath:/static/javascript/")
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
		
		// ## swagger config ##
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
	}
	
}

fun main(args: Array<String>) {
    runApplication<BlogkotlinApplication>(*args)
}