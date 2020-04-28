package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.service.PostImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/postImage")
class PostImageController {

    @Autowired
    lateinit var postImageService: PostImageService

    @PostMapping("/save")
    fun uploadImage(@RequestParam(value = "id") id: Long,
                    @RequestParam("image") multiPartFile: MultipartFile): ResponseEntity<String> {

        val url = postImageService.save(id, multiPartFile)

        return ResponseEntity(url, HttpStatus.OK)
    }

}