package br.com.dblogic.blogkotlin

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class BlogkotlinApplication

fun main(args: Array<String>) {
	runApplication<BlogkotlinApplication>(*args)
}
