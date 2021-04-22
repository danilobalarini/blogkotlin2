package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    fun findByName(name: String): Tag

    fun findAllByPostsId(post: Long) : Set<Tag>

}