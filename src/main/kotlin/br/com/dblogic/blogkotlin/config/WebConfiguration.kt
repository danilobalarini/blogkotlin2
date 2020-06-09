package br.com.dblogic.blogkotlin.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer {
	
	private val logger = LoggerFactory.getLogger(WebConfiguration::class.java)
	
	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {

		logger.info("####### Entering ResourceHandlers configurations #######")
		
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
		registry.addResourceHandler("/javascript/**").addResourceLocations("classpath:/static/javascript/")
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
		registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/images/favicon/favicon.ico")
	}
	
}