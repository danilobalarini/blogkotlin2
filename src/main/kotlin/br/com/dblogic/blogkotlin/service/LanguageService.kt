package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Language
import br.com.dblogic.blogkotlin.repository.LanguageRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LanguageService {

    private val logger = LoggerFactory.getLogger(LanguageService::class.java)

    @Autowired
    lateinit var languageRepository: LanguageRepository

    fun findById(id: Long) : Language {
        return languageRepository.findById(id).orElse(Language())
    }

    fun save(language: Language) : Language {
        return languageRepository.saveAndFlush(language)
    }

}