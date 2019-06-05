package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.CascadeType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "tb_user")
data class User (@Id
		    	 @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
				 @GenericGenerator(name = "native", strategy = "native")
		   		 val id: Int = 0,

				 @Column(nullable = false)
		   		 var screenUsername: String = "someuser", 
	
				 @OneToMany(mappedBy = "user",
						 	cascade = arrayOf(CascadeType.ALL),
						 	orphanRemoval = false)
				 var comments: MutableList<Comment> = mutableListOf<Comment>()) : DateAudit() {
	
	fun addComment(comment: Comment) {
		comments.add(comment)
		comment.user = this
	}
	
	fun removeComment(comment: Comment) {
		comments.remove(comment)
		comment.user = this
	}

}