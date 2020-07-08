package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.CaptchaEvent
import br.com.dblogic.blogkotlin.model.CaptchaResponse
import br.com.dblogic.blogkotlin.model.Contact
import br.com.dblogic.blogkotlin.model.facade.ContactFacade
import br.com.dblogic.blogkotlin.recaptcha.GoogleResponse
import br.com.dblogic.blogkotlin.repository.CaptchaResponseRepository
import br.com.dblogic.blogkotlin.repository.ContactRepository
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContactService {

    private val logger = LoggerFactory.getLogger(ContactService::class.java)

    @Autowired
    lateinit var contactRepository: ContactRepository

    @Autowired
    lateinit var captchaService: CaptchaService

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
        if(StringUtils.isBlank(contactFacade.name)) return true
        if(StringUtils.isBlank(contactFacade.email)) return true
        if(StringUtils.isBlank(contactFacade.message)) return true
        return false
    }

}