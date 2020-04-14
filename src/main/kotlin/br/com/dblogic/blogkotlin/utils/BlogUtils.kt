package br.com.dblogic.blogkotlin.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.nio.file.Paths

@Component
class BlogUtils {

    @Value("\${blog.name.directory}")
    lateinit var blogFolderName: String

    fun createInBlogDir(): Path {
        val currentWorkingDir: Path = Paths.get("").toAbsolutePath()
        val dirpath = "$currentWorkingDir/$blogFolderName/"

        return Paths.get(dirpath)
    }

    fun appendToBlogDir(pathToAdd: String): String {
        val currentWorkingDir: Path = Paths.get("").toAbsolutePath()
        val dirpath = "$currentWorkingDir/$blogFolderName/$pathToAdd"

        return dirpath
    }

}