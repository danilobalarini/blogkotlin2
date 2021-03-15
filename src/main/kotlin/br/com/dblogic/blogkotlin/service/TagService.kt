package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.exception.DeleteTagException
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.model.facade.TagFacade
import br.com.dblogic.blogkotlin.repository.TagRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.stream.Collectors

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
        logger.info("tag id: ${tag.id}")
        logger.info("tag name: ${tag.name}")

        return tagRepository.save(tag)
    }

    // TODO this will be cache someday
    fun displayMultipleselect(post: Post) : Set<TagFacade> {
        return mutableSetOf<TagFacade>()
    }

    fun delete(tag: Tag) {
        try {
            tagRepository.delete(tag)
        } catch (e: Exception) {
            throw DeleteTagException("Ocorreu um erro ao tentar apagar a tag")
        }
    }

    fun onlyIds(tags: MutableSet<Tag>): String {
        return tags
                .stream()
                .map { t -> java.lang.String.valueOf(t.id) }
                .collect(Collectors.joining(","))
    }

    fun toSetFacade(tags: MutableSet<Tag>): MutableSet<TagFacade> {
        val tagsFacade = mutableSetOf<TagFacade>()
        for(t in tags) {
            val facade = TagFacade(t.id, t.name, false)
            tagsFacade.add(facade)
        }
        return tagsFacade
    }

}
