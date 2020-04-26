package br.com.dblogic.blogkotlin.model.facade

import br.com.dblogic.blogkotlin.model.Post

data class FrontPagePostFacade(val post: Post = Post(),
							   val commentCount: Long = 0,
							   val coverImage: String) {
}