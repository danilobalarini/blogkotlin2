package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {

}