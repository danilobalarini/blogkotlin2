package br.com.dblogic.blogkotlin.model.facade

data class CommentFormFacade(var post: Long = 0,
                             var name: String = "",
                             var email: String = "",
                             var text: String = "",
                             var response: String = "") {
}
