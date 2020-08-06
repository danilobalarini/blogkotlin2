package br.com.dblogic.blogkotlin.model.facade

import java.time.Instant

data class PostFacade(val id: Long = 0,
                      var title: String = "",
                      var review: String = "",
                      var isDraft: Boolean = true,
                      var createdAt: Instant = Instant.now(),
                      var comments: Int = 0,
                      var tags: MutableSet<TagFacade> = mutableSetOf<TagFacade>(),
                      var coverImage: String = "") {
}