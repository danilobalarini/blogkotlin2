package br.com.dblogic.blogkotlin.model

import br.com.dblogic.blogkotlin.model.facade.FrontPagePostFacade

data class FrontPageFacade(val frontPagePost: FrontPagePostFacade,
						   val frontPagePosts: List<FrontPagePostFacade>) {
			
}