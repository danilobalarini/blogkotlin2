package br.com.dblogic.blogkotlin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.builders.PathSelectors

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

	@Bean
	fun swagger(): Docket = Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("br.com.dblogic.blogkotlin.controller"))
			.paths(PathSelectors.any())
			.build()

}