package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.model.facade.TagFacade
import br.com.dblogic.blogkotlin.service.TagService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tags")
class TagController {

    private val logger = LoggerFactory.getLogger(TagController::class.java)

    @Autowired
    lateinit var tagService: TagService

    @PostMapping("/save")
    fun save(@RequestBody tagFacade: TagFacade) {
        logger.info("tagFacade id: " + tagFacade.id)
        val tag = if(tagFacade.id == 0L) Tag() else tagService.findById(tagFacade.id)
        tag.name = tagFacade.name
        logger.info("tag id: " + tag.id)
        tagService.save(tag)
    }

    @GetMapping("/delete")
    fun delete(@RequestParam(defaultValue = "0") id: Long) {
        tagService.delete(Tag(id))
    }

}