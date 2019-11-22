package br.com.dblogic.blogkotlin.model.facade

import br.com.dblogic.blogkotlin.model.Post

data class PostCommentCountFacade(val post: Post = Post(),
								  val commentCount: Long = 0,
								  val coverImage: ByteArray = ByteArray(0)) {
}