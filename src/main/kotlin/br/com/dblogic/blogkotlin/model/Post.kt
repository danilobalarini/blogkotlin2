package br.com.dblogic.blog.model

import org.hibernate.annotations.GenericGenerator

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.GenerationType

@Entity
@Table(name = "tb_post")
class Post (@Id
		   	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
		   	@GenericGenerator(name = "native", strategy = "native")
		   	val id: Long = 0L,
		   	val title: String = "",
		   	val inclusionDate: LocalDateTime = LocalDateTime.now(),
		   	val editDate: LocalDateTime = LocalDateTime.now()) {
	
/*	    constructor(id: Long, post: Post) : this(id) {

	    } */
	
}