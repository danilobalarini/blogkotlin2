package br.com.dblogic.blogkotlin.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect

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
		
		// ## swagger config ##
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/")
	}
	
}