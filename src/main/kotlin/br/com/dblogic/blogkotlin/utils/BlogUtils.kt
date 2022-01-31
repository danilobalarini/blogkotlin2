package br.com.dblogic.blogkotlin.utils

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.PostFacade
import br.com.dblogic.blogkotlin.model.facade.PostSearchFacade
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.text.Normalizer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.imageio.ImageIO

@Component
class BlogUtils {

    private val logger = LoggerFactory.getLogger(BlogUtils::class.java)

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
        var s1 = s
        s1 = Normalizer.normalize(s1, Normalizer.Form.NFD)
        s1 = s1.replace(onlyUnicode, "")
        return s1
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

    fun defaultCoverImage(text: String): ByteArray {

        val image = BufferedImage(1300, 860, BufferedImage.TYPE_INT_RGB)
        val g2d: Graphics2D = image.createGraphics()
        g2d.color = Color.WHITE
        g2d.fillRect(0, 0, 1300, 860)

        g2d.color = Color.BLACK
        g2d.font = Font("Georgia", Font.BOLD, 36)
        g2d.drawString(text, 200, 200)

        val baos = ByteArrayOutputStream()
        ImageIO.write(image, "jpg", baos)
        baos.flush()
        val imageBA = baos.toByteArray()
        baos.close()

        return imageBA
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