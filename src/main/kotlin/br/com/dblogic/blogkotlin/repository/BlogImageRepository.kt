package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.BlogImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BlogImageRepository : JpaRepository<BlogImage, Long> {

    override fun findById(id: Long): Optional<BlogImage>

}