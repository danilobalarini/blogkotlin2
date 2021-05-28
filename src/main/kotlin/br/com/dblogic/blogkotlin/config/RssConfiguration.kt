package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.service.PostService
import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Item
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.feed.AbstractRssFeedView
import java.sql.Date
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RssFeedView : AbstractRssFeedView() {

    @Autowired
    lateinit var postService: PostService

    override fun buildFeedMetadata(model: Map<String, Any>, feed: Channel, request: HttpServletRequest) {
        feed.title = "DbLogic Blog"
        feed.description = "Maybe useful content"
        feed.link = "http://localhost:8090"
    }

    override fun buildFeedItems(model: Map<String, Any>, request: HttpServletRequest, response: HttpServletResponse): List<Item> {

        val post = postService.findById(9)
        val postFacade = postService.postToFacade(post)

        val entryOne = Item()
            entryOne.title = post.title
            entryOne.author = "Danilo"
            entryOne.link = "http://localhost:8090/postid/9"
            entryOne.pubDate = Date.from(Instant.parse("2017-12-19T00:00:00Z"))
        return listOf(entryOne)
    }
}