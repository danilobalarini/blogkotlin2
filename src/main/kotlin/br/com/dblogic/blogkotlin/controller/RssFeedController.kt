package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.config.RssFeedView
import com.rometools.rome.feed.rss.*
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URL
import java.util.*

@RestController
class RssFeedController {

    @Autowired
    lateinit var view: RssFeedView

    @GetMapping(path = ["/rss"])
    fun rss(): Channel {

        val channel = Channel()
        channel.feedType = "rss_2.0"
        channel.title = "HowToDoInJava Feed"
        channel.description = "Different Articles on latest technology"
        channel.link = "https://howtodoinjava.com"
        channel.uri = "https://howtodoinjava.com"
        channel.generator = "In House Programming"

        val image = Image()
        image.url = "https://howtodoinjava.com/wp-content/uploads/2015/05/howtodoinjava_logo-55696c1cv1_site_icon-32x32.png"
        image.title = "HowToDoInJava Feed"
        image.height = 32
        image.width = 32
        channel.image = image

        val postDate = Date()
        channel.pubDate = postDate

        val item = Item()
        item.author = "Lokesh Gupta"
        item.link = "https://howtodoinjava.com/spring5/webmvc/spring-mvc-cors-configuration/"
        item.title = "Spring CORS Configuration Examples"
        item.uri = "https://golb.hplar.ch/p/1"
        item.comments = "https://howtodoinjava.com/spring5/webmvc/spring-mvc-cors-configuration/#respond"

        val category = Category()
        category.value = "CORS"
        item.categories = Collections.singletonList(category)

        val descr = Description()
        descr.value = ("CORS helps in serving web content from multiple domains into browsers who usually have the same-origin security policy. In this example, we will learn to enable CORS support in Spring MVC application at method and global level."
                + "The post <a rel=\"nofollow\" href=\"https://howtodoinjava.com/spring5/webmvc/spring-mvc-cors-configuration/\">Spring CORS Configuration Examples</a> appeared first on <a rel=\"nofollow\" href=\"https://howtodoinjava.com\">HowToDoInJava</a>.")
        item.description = descr
        item.pubDate = postDate

        channel.items = Collections.singletonList(item)
        //Like more Entries here about different new topics
        return channel
    }

    @GetMapping("/consume")
    fun read() {
        try {
            val url = "http://localhost:8090/rss"
            XmlReader(URL(url)).use { reader ->
                val feed = SyndFeedInput().build(reader)
                println(feed.title)
                println("***********************************")
                for (entry in feed.entries) {
                    println(entry)
                    println("***********************************")
                }
                println("Done")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}