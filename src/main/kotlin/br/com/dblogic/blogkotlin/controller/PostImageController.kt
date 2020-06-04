package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.UploadForm
import br.com.dblogic.blogkotlin.service.PostImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/postImage")
class PostImageController {

    @Autowired
    lateinit var postImageService: PostImageService

    @ResponseBody
    @PostMapping("/updateCoverImage")
    fun updateCoverImage(@ModelAttribute form: UploadForm) : ResponseEntity<String> {

        // postImageService.updateCoverImage(form.id, form.coverImage[0])
        val url = postImageService.updateCoverImage(form.id, form.coverImage[0])

        return ResponseEntity(url, HttpStatus.OK)
    }

    @PostMapping("/save")
    fun uploadImage(@RequestParam(value = "id") id: Long,
                    @RequestParam("image") multiPartFile: MultipartFile): ResponseEntity<String> {

        val url = postImageService.save(id, multiPartFile)

        return ResponseEntity(url, HttpStatus.OK)
    }

}