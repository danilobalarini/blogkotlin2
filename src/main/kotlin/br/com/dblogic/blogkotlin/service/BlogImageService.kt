package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.BlogImage
import br.com.dblogic.blogkotlin.repository.BlogImageRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class BlogImageService {

    @Autowired
    lateinit var blogImageRepository: BlogImageRepository

    fun findById(id: Long) : BlogImage {
        return blogImageRepository.findById(id).get()
    }

    fun store(multiPartFile: MultipartFile) {

        val bi = BlogImage()
        bi.filename = multiPartFile.originalFilename.toString()
        bi.description = StringUtils.EMPTY
        bi.image = multiPartFile.bytes

        blogImageRepository.save(bi)
    }

}