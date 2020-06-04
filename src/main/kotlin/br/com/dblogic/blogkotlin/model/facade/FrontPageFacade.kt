package br.com.dblogic.blogkotlin.model

import br.com.dblogic.blogkotlin.model.facade.PostFacade

data class FrontPageFacade(val postFacade: PostFacade,
						   val postFacades: List<PostFacade>) {
			
}