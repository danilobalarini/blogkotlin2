package br.com.dblogic.blogkotlin

import br.com.dblogic.blogkotlin.model.Post
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.multipart.MultipartFile

@SpringBootApplication
class BlogkotlinApplication

fun main(args: Array<String>) {
	runApplication<BlogkotlinApplication>(*args)
}