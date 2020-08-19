package br.com.dblogic.blogkotlin.model.facade

data class PostSearchFacade(var title: String = "",
                            var review: String = "",
                            var response: String = "",
                            var pagenumber: Int = 0,
                            var isFirst: Boolean = false,
                            var isLast: Boolean = false,
                            var totalPages: Int = 0,
                            var posts: MutableList<PostFacade> = mutableListOf<PostFacade>()) {
}