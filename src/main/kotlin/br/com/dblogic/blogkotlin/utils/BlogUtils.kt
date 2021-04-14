package br.com.dblogic.blogkotlin.utils

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.model.facade.PostSearchFacade
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.nio.file.Paths
import java.text.Normalizer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class BlogUtils {

    private val logger = LoggerFactory.getLogger(BlogUtils::class.java)

    @Value("\${blog.directory.name}")
    lateinit var blogDirName: String

    @Value("\${blog.root.folder}")
    lateinit var blogFolderName: String

    private final val onlyCharsAndNumbers = "[^A-Za-z0-9]".toRegex()
    private final val onlyUnicode = "[\\p{InCombiningDiacriticalMarks}]".toRegex()

    fun getBlogDir(): Path? {
        val blogdir: Path = Paths.get("").toAbsolutePath()
        return Paths.get("$blogdir/$blogFolderName").toAbsolutePath()
    }

    fun appendToBlogDir(pathToAdd: String): String {
        val currentWorkingDir: Path = Paths.get("").toAbsolutePath()
        return "$currentWorkingDir/$blogFolderName/$pathToAdd"
    }

    fun stripAccents(s: String): String {
        var s = s
        s = Normalizer.normalize(s, Normalizer.Form.NFD)
        s = s.replace(onlyUnicode, "")
        return s
    }

    fun getTitle(id: Long, blogtitle: String, createdAt: Instant): String {
        // always put the date in the last part
        // id-title-date
        // date=YYYYMMDD
        // this is documentation
        val titleWithoutAccents = stripAccents(blogtitle)
        var title = ""

        for(s in titleWithoutAccents.split(" ")) {
            title += s.replace(onlyCharsAndNumbers, "") + "-"
        }

        return "$id-$title${toDateString(createdAt)}"
    }

    fun getDirectoryNameFromPost(post: Post): String {
        val titleWithoutAccents = stripAccents(post.title)
        var title = ""

        for(s in titleWithoutAccents.split(" ")) {
            title += s.replace(onlyCharsAndNumbers, "") + "-"
        }

        return post.id.toString() + "-" + title + toDateString(post.createdAt)
    }

    fun getDirectoryPathFromPost(p: Post): Path {
        val dir = appendToBlogDir(getDirectoryNameFromPost(p))
        return Paths.get("$dir")
    }

    fun postSearchFacadeWithoutList(facade: PostSearchFacade): PostSearchFacade {
        return PostSearchFacade(facade.title,
                facade.review,
                facade.response,
                facade.pagenumber,
                facade.isFirst,
                facade.isLast,
                if(facade.totalPages > 0) (facade.totalPages-1) else 0,
                mutableListOf<PostFacade>())
    }

    private fun toDateString(date: Instant) : String {

        val zone = ZoneOffset.UTC

        val year = LocalDateTime.ofInstant(date, zone).year.toString()
        val month = LocalDateTime.ofInstant(date, zone).monthValue.toString().padStart(2, '0')
        val day = LocalDateTime.ofInstant(date, zone).dayOfMonth.toString().padStart(2, '0')

        return "$year$month$day"
    }

}