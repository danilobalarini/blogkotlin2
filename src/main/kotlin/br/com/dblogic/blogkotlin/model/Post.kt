package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(name = "tb_post")
class Post (@Id
		    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
			@GenericGenerator(name = "native", strategy = "native")
		   	var id: Long = 0L,
			
			val title: String = "",
			
			@Lob
			val text: String = "") : DateAudit() {

}