package br.com.dblogic.blogkotlin.model.facade

data class FrontPageFacade(val postFacade: PostFacade,
						   val postFacades: List<PostFacade>) {
			
}