package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Language
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LanguageRepository : JpaRepository<Language, Long> {

}