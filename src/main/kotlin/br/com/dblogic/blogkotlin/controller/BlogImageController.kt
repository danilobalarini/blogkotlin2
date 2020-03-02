package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.service.BlogImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/blogimage")
class BlogImageController {

    @Autowired
    lateinit var blogImageService: BlogImageService

    @PostMapping("/save")
    fun uploadImage(@RequestParam("image") multiPartFile: MultipartFile): ResponseEntity<HttpStatus> {

        blogImageService.store(multiPartFile)

        return ResponseEntity.ok(HttpStatus.OK)
    }

}