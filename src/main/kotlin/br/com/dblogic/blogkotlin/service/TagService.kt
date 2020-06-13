package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.repository.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagService {

    @Autowired
    lateinit var tagRepository: TagRepository

    fun findAll() : MutableSet<Tag> = mutableSetOf<Tag>()

    fun save(tag: Tag) : Tag  {
        return tagRepository.save(tag)
    }

}
