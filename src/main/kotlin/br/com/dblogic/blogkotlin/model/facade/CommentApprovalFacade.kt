package br.com.dblogic.blogkotlin.model.facade

import java.time.Instant

data class CommentApprovalListFacade(val id: Long = 0,
                                     val postName: String = "",
                                     val datePostUpdated: Instant = Instant.now()) {

}