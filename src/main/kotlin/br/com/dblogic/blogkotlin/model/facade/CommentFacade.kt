package br.com.dblogic.blogkotlin.model.facade

import java.time.Instant

data class CommentFacade (val id: Long = 0,
                          var post: Long = 0,
                          var name: String = "",
                          var email: String = "",
                          var text: String = "",
                          var createdAt: Instant = Instant.now(),
                          var approved: Boolean = false) {

}