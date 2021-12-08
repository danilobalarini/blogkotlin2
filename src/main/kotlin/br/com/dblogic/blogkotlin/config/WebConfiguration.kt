package br.com.dblogic.blogkotlin.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer {
	
	private val logger = LoggerFactory.getLogger(WebConfiguration::class.java)

	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
		logger.info("####### Entering ResourceHandlers configurations #######")
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
		registry.addResourceHandler("/blog/**").addResourceLocations("file:src/main/resources/blog/")
	}

	@Bean
	fun restTemplate() : RestTemplate {
		return RestTemplate()
	}

}