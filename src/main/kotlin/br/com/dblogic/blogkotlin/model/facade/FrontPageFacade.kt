package br.com.dblogic.blogkotlin.model

import br.com.dblogic.blogkotlin.model.facade.PostCommentCountFacade

data class FrontPageFacade(val post: PostCommentCountFacade,
						   val posts: List<PostCommentCountFacade>) {
			
}