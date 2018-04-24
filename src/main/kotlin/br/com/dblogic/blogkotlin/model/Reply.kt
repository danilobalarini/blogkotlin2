package br.com.dblogic.blog.model

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn

@Entity
@Table(name = "tb_reply")
class Reply(@Id
		   	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
		   	@GenericGenerator(name = "native", strategy = "native")
		   	val id: Long = 0L,
			
			@Lob
			val reply: String = "",
			 
			val inclusionDate: LocalDateTime = LocalDateTime.now(),
			val editDate: LocalDateTime = LocalDateTime.now(),
			
			@ManyToOne
			val post: Post = Post())