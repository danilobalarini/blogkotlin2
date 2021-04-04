package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.Cookie

@Service
class BlogService {

    @Autowired
    lateinit var postService: PostService

    fun managePageViewCookie(postsvisited: String, post: Post): Cookie {

        var visitedlist = ""
        var isVisited = false
        val id = post.id.toString()
        if(postsvisited.isNotBlank()) {
            val posts = postsvisited.split(",")
            for(p in posts) {
                if(id == p.trim()) {
                    isVisited = true
                    visitedlist = postsvisited
                    break
                }
            }

            if(!isVisited) {
                postService.sumPageView(post.id)
                visitedlist = "$postsvisited, $id"
            }
        } else {
            postService.sumPageView(post.id)
            visitedlist = "$id"
        }

        val cookie = Cookie("postsvisited", visitedlist)
        cookie.maxAge = (7 * 24 * 60 * 60)

        return cookie
    }

}