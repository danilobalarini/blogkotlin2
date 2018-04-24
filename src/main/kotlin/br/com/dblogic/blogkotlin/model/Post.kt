package br.com.dblogic.blogkotlin.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_post")
class Post (@Id
		   	var id: Long = 0L,
		   	val title: String = "",
		   	val inclusionDate: LocalDateTime = LocalDateTime.now(),
		   	val editDate: LocalDateTime = LocalDateTime.now()) {
		
}