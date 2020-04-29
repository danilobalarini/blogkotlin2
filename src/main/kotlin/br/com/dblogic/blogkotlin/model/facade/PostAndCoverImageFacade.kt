package br.com.dblogic.blogkotlin.model.facade

import br.com.dblogic.blogkotlin.model.Post

data class PostAndCoverImageFacade(val post: Post = Post(),
                                   val coverImage: String) {
}