package br.com.dblogic.blogkotlin.model

import br.com.dblogic.blogkotlin.model.Comment
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
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.CascadeType

@Entity
@Table(name = "tb_post")
data class Post (@Id
		    	 @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
				 @GenericGenerator(name = "native", strategy = "native")
		   		 val id: Int = 0,

		   		 var title: String = "",

		   		 @Lob
		   		 var text: String = "",
				 
				 @OneToMany(mappedBy = "post",
						 	cascade = arrayOf(CascadeType.ALL),
						 	orphanRemoval = true)
				 var comments: MutableList<Comment> = mutableListOf<Comment>()) : DateAudit() {

	constructor(title: String, text: String): this() {
		this.title = title
		this.text = text
	}

	constructor(title: String, text: String, createdAt: Instant): this() {
		this.title = title
		this.text = text
		this.createdAt = createdAt
	}
	
	fun addComment(comment: Comment) {
		comments.add(comment)
		comment.post = this
	}
	
	fun removeComment(comment: Comment) {
		comments.remove(comment)
		comment.post = this
	}

}