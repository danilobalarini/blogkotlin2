package br.com.dblogic.blogkotlin.model.facade

data class TagFacade(val id: Int = 0,
                     var name: String = "", 
                     val selected: Boolean = false) {
}