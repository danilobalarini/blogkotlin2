package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.CaptchaEvent
import br.com.dblogic.blogkotlin.model.Contact
import br.com.dblogic.blogkotlin.model.facade.ContactFacade
import br.com.dblogic.blogkotlin.repository.ContactRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ContactService {

    private val logger = LoggerFactory.getLogger(ContactService::class.java)

    @Autowired
    lateinit var contactRepository: ContactRepository

    @Autowired
    lateinit var captchaService: CaptchaService

    fun countCheckedFalse() : Long {
        return contactRepository.countByCheckedFalse()
    }

    fun save(contactFacade: ContactFacade) {

        logger.info("is any field empty? " + !isAnyEmpty(contactFacade))
        if(!isAnyEmpty(contactFacade)) {

            logger.info("saving contact")
            val c = facadeToContact(contactFacade)
            val contact = contactRepository.save(c)

            logger.info("saving captcha response of contact")
            captchaService.save(contactFacade.response, CaptchaEvent.CONTACT, contact.id)
        }
    }

    private fun facadeToContact(contactFacade: ContactFacade): Contact {
        return Contact(0L, contactFacade.name, contactFacade.email, contactFacade.message)
    }

    private fun isAnyEmpty(contactFacade: ContactFacade): Boolean {
        if(contactFacade.name.isBlank()) return true
        if(contactFacade.email.isBlank()) return true
        if(contactFacade.message.isBlank()) return true
        return false
    }

}