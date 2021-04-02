package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.model.facade.ContactFacade
import br.com.dblogic.blogkotlin.service.ContactService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contact")
class ContactController {

    @Autowired
    lateinit var contactService: ContactService

    @PostMapping("/add")
    fun addContact(@RequestBody contactFacade: ContactFacade): ResponseEntity<String> {
        contactService.save(contactFacade)

        return ResponseEntity("ok", HttpStatus.OK)
    }

}