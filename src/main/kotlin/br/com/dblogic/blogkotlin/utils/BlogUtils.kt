package br.com.dblogic.blogkotlin.utils

import br.com.dblogic.blogkotlin.model.Post
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class BlogUtils {

    private val logger = LoggerFactory.getLogger(BlogUtils::class.java)

    @Value("\${blog.root.folder}")
    lateinit var blogFolderName: String

    fun appendToBlogDir(pathToAdd: String): String {
        val currentWorkingDir: Path = Paths.get("").toAbsolutePath()
        val dirpath = "$currentWorkingDir/$blogFolderName/$pathToAdd"

        return dirpath
    }

    fun getDirectoryNameFromPost(post: Post): String {
        // always put the date in the last part
        // id-title-date
        // date=YYYYMMDD
        // this is documentation
        val titleWithoutAccents = StringUtils.stripAccents(post.title)
        var title = ""

        for(s in StringUtils.split(titleWithoutAccents, StringUtils.SPACE)) {
            title = title + s.replace("[^A-Za-z0-9]".toRegex(), StringUtils.EMPTY) + "-"
        }

        return post.id.toString() + "-" + title + toDateString(post.createdAt)
    }

    private fun toDateString(date: Instant) : String {

        val zone = ZoneOffset.UTC

        val year = LocalDateTime.ofInstant(date, zone).year.toString()
        val month = StringUtils.leftPad(LocalDateTime.ofInstant(date, zone).monthValue.toString(), 2, "0")
        val day = StringUtils.leftPad(LocalDateTime.ofInstant(date, zone).dayOfMonth.toString(), 2, "0")

        return "$year$month$day"
    }

}