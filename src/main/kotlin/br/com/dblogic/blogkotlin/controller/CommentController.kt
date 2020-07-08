package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.facade.CommentFacade
import br.com.dblogic.blogkotlin.model.facade.CommentFormFacade
import br.com.dblogic.blogkotlin.service.CommentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/comment")
class CommentController {

    private val logger = LoggerFactory.getLogger(CommentController::class.java)

    @Autowired
    lateinit var commentService: CommentService

    @PostMapping("/save")
    fun save(@RequestBody facade: CommentFormFacade): CommentFacade {
        logger.info("entering save comment")
        logger.info("facade.post: ${facade.post}")
        logger.info("facade.name: ${facade.name}")
        logger.info("facade.email: ${facade.email}")
        logger.info("facade.text: ${facade.text}")
        logger.info("facade.response: ${facade.response}")
        return commentService.save(facade)
    }

}