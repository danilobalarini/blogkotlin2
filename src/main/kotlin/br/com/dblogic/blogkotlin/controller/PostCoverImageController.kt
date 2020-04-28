package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.service.PostCoverImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/coverimage")
class PostCoverImageController {

    @Autowired
    lateinit var postCoverImageService: PostCoverImageService

    @PostMapping("/update")
    fun uploadImage(@RequestParam(value = "post") id: Long,
                    @RequestParam("image") multiPartFile: MultipartFile): ResponseEntity<HttpStatus> {

        postCoverImageService.updateFrontPageCoverImage(id, multiPartFile)

        return ResponseEntity.ok(HttpStatus.OK)
    }

}