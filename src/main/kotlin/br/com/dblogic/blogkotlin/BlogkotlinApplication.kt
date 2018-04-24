package br.com.dblogic.blogkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogkotlinApplication

fun main(args: Array<String>) {
    runApplication<BlogkotlinApplication>(*args)
}
