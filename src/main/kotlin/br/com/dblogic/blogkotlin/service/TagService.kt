package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.exception.DeleteTagException
import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.Tag
import br.com.dblogic.blogkotlin.model.facade.TagFacade
import br.com.dblogic.blogkotlin.repository.TagRepository
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
        logger.info("tag id: ${tag.id}")
        logger.info("tag name: ${tag.name}")

        return tagRepository.save(tag)
    }

    fun findByPostId(post: Long) : Set<Tag> {
        return tagRepository.findAllByPostsId(post)
    }

    fun delete(tag: Tag) {
        try {
            tagRepository.delete(tag)
        } catch (e: Exception) {
            throw DeleteTagException("Ocorreu um erro ao tentar apagar a tag")
        }
    }

    fun toSetFacade(tags: MutableSet<Tag>): MutableSet<TagFacade> {
        val tagsFacade = mutableSetOf<TagFacade>()
        for(t in tags) {
            val facade = TagFacade(t.id, t.name)
            tagsFacade.add(facade)
        }
        return tagsFacade
    }

    fun tagsFacadeByPost(post: Post): MutableSet<TagFacade> {
        val tagsFacade = mutableSetOf<TagFacade>()
        val postTags = tagRepository.findAllByPostsId(post.id)
        for(t in tagRepository.findAll()) {
            tagsFacade.add(TagFacade(t.id, t.name, postTags.contains(t)))
        }
        return tagsFacade
    }

}