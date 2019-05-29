package br.com.dblogic.blogkotlin.model.facade

import br.com.dblogic.blogkotlin.model.Post

data class PostCommentCountFacade(val post: Post,
								  val commentCount: Long) {
}