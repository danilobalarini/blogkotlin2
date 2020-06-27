package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.CaptchaResponse
import org.springframework.data.jpa.repository.JpaRepository

interface CaptchaResponseRepository : JpaRepository<CaptchaResponse, Long> {

}