package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.GenerationType
import javax.persistence.FetchType

@Entity
@Table(name = "tb_comment")
data class Comment(@Id
		    	   @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
				   @GenericGenerator(name = "native", strategy = "native")
		   		   val id: Long = 0L,
				   
				   // TODO isso vai virar um relacionamento
				   @Column(nullable = false)
				   var author: String = "",
				   
				   @Lob
		   		   var text: String = "",
				   
				   @ManyToOne(fetch = FetchType.LAZY)
				   @JoinColumn(name = "post_id")
				   var post: Post = Post(),
				   
   				   @CreatedDate
				   @Column(nullable = false, updatable = false)
				   val createdAt: Instant = Instant.now(), 

				   @LastModifiedDate
				   @Column(nullable = false)
				   var updatedAt: Instant = Instant.now()) {
	
	constructor(author: String, text: String, post: Post): this() {
		this.author = author
		this.text = text
		this.post = post
	}

}