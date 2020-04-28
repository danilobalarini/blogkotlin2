package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostCoverImage
import br.com.dblogic.blogkotlin.model.PostImage
import br.com.dblogic.blogkotlin.repository.PostImageRepository
import br.com.dblogic.blogkotlin.utils.BlogUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class PostImageService {

    private val logger = LoggerFactory.getLogger(PostImageService::class.java)

    @Autowired
    lateinit var postService: PostService

    @Autowired
    lateinit var blogUtils: BlogUtils

    @Autowired
    lateinit var postImageRepository: PostImageRepository

    @Value("\${blog.directory.name}")
    lateinit var blogDirectoryName: String

    @Value("\${server.port}")
    lateinit var port: String

    fun findById(id: Long) : PostImage {
        return postImageRepository.findById(id).get()
    }

    fun save(id: Long, multiPartFile: MultipartFile): String {

        val post = postService.findById(id)
        val name = getName(multiPartFile)
        val image = if (multiPartFile.bytes.isNotEmpty()) multiPartFile.bytes else byteArrayOf(0)

        val postImage = postImageRepository.save(PostImage(id, name, "", image, false, post))

        val title = blogUtils.getDirectoryNameFromPost(post)
        logger.info("### title: $title")

        val newimage = Paths.get(blogUtils.appendToBlogDir("$title/$name"))
        logger.info("### path to be created: $newimage")
        Files.write(newimage, postImage.image, StandardOpenOption.CREATE)

        val url = "http://localhost:$port/$blogDirectoryName/$title/$name"
        logger.info("URL: $url")

        return url
    }

    private fun getName(multiPartFile: MultipartFile) : String {

        if(!multiPartFile.isEmpty) {
            if(multiPartFile.originalFilename?.isNotBlank()!!) {
                return multiPartFile.originalFilename!!
            }
        }
        return ""
    }


}