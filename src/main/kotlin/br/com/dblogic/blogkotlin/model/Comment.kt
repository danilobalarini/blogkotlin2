package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "tb_comment")
data class Comment(@Id
		    	   @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_comment")
				   @GenericGenerator(name = "seq_comment", strategy = "native")
		   		   val id: Long = 0L,

				   @Lob
		   		   var text: String = "",
				   
				   @ManyToOne(fetch = FetchType.LAZY)
				   @JoinColumn(name = "post_id")
				   var post: Post? = Post(),
				   
				   @ManyToOne(fetch = FetchType.LAZY)
				   @JoinColumn(name = "user_id")
				   var user: User = User()) : DateAudit() {
	
	constructor(text: String, post: Post, user: User): this() {
		this.text = text
		this.post = post
		this.user = user
	}

	constructor(text: String, post: Post, user: User, createdAt: Instant): this() {
		this.text = text
		this.post = post
		this.user = user
		this.createdAt = createdAt
	}
	
	fun addComment(text: String, post: Post, user: User) {
		this.text = text
		this.post = post
		this.user = user
	}
	
	fun removeComment(text: String, post: Post, user: User) {
		this.text = text
		this.post = post
		this.user = user
	}

	override fun toString(): String {
		return super.toString()
	}

}