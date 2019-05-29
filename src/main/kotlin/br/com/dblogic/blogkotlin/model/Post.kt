package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(name = "tb_post")
data class Post (@Id
		    	 @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
				 @GenericGenerator(name = "native", strategy = "native")
		   		 val id: Long = 0L,

		   		 var title: String = "",

		   		 @Lob
		   		 var text: String = "",

				 @CreatedDate
				 @Column(nullable = false, updatable = false)
				 val createdAt: Instant = Instant.now(), 

				 @LastModifiedDate
				 @Column(nullable = false)
				 var updatedAt: Instant = Instant.now()) {

	constructor(title: String, text: String): this() {
		this.title = title
		this.text = text
	}

}