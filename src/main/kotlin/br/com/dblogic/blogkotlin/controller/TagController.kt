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
    fun save(@RequestBody tagFacade: TagFacade): TagFacade {

        logger.info("bateu no controller")

        val tag = Tag(tagFacade.name)
        val newtag = tagService.save(tag)

        return TagFacade(newtag.id, newtag.name)
    }

    @GetMapping("/delete")
    fun delete(@RequestParam(defaultValue = "0") id: Long){
        if(id != 0L) {
            tagService.delete(Tag(id))
        }
    }

}