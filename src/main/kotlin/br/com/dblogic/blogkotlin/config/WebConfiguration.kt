package br.com.dblogic.blogkotlin.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.thymeleaf.spring5.view.ThymeleafViewResolver

@Configuration
@EnableWebMvc
class WebConfiguration : WebMvcConfigurer {
		
	private val log = LoggerFactory.getLogger(WebConfiguration::class.java)
	
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