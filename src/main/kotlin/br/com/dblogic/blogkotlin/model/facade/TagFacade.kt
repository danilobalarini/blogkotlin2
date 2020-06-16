package br.com.dblogic.blogkotlin.model.facade

data class TagFacade(val id: Long = 0,
                     var name: String = "", 
                     val selected: Boolean = false) {
}