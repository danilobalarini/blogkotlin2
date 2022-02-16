package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.facade.UploadFormFacade
import br.com.dblogic.blogkotlin.service.PostImageService
import br.com.dblogic.blogkotlin.service.PostService
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

    @Autowired
    lateinit var postService: PostService

    @ResponseBody
    @PostMapping("/updateCoverImage")
    fun updateCoverImage(@ModelAttribute form: UploadFormFacade) : ResponseEntity<String> {
        val post = postService.findById(form.id)
        val deleteImage = postImageService.findCoverImage(post)
        val postImage = postImageService.updateCoverImage2(post, form.coverImage[0])
        val url = postService.updateCoverImage(post, postImage, deleteImage)

        return ResponseEntity(url, HttpStatus.OK)
    }

    @PostMapping("/save")
    fun uploadImage(@RequestParam(value = "id") id: Long,
                    @RequestParam("image") multiPartFile: MultipartFile): ResponseEntity<String> {
        val url = postImageService.save(postService.findById(id), multiPartFile)
        return ResponseEntity(url, HttpStatus.OK)
    }

}