package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.model.facade.TagFacade
import br.com.dblogic.blogkotlin.repository.TagRepository
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagService {

    private val logger = LoggerFactory.getLogger(TagService::class.java)

    @Autowired
    lateinit var tagRepository: TagRepository

    fun findAll() : MutableList<Tag> {
        return tagRepository.findAll()
    }

    fun findById(id: Long): Tag {
        return tagRepository.findById(id)
                            .orElse(Tag())
    }

    fun findByName(tagname: String): Tag {
        return tagRepository.findByName(tagname)
    }

    fun save(tag: Tag) : Tag  {
        logger.info("bateu no service")

        val tag1 = tagRepository.save(tag)
        logger.info("tag id: ${tag1.id}")
        logger.info("tag name: ${tag1.name}")

        return tag1
    }

    // TODO this will be cache someday
    fun displayMultipleselect(post: Post) : Set<TagFacade> {

        return mutableSetOf<TagFacade>()
    }

    fun delete(tag: Tag) {
        tagRepository.delete(tag)
    }

    fun onlyIds(tags: MutableSet<Tag>): String {

        var csv = ""
        var comma = ""
        for(t in tags) {
            csv+="$comma${t.id}"
            if(!StringUtils.equals(comma,",")) {
                comma = ","
            }
        }

        return csv
    }

}
