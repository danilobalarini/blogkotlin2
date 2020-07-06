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

				   @ManyToOne(fetch = FetchType.LAZY)
				   @JoinColumn(name = "post_id")
				   var post: Post? = Post(),
				   
				   var name: String = "",

				   var email: String = "",

				   @Lob
				   var text: String = "",

				   var isApproved: Boolean = false): DateAudit() {

	constructor(post: Post, text: String, name: String, email: String, isApproved: Boolean, createdAt: Instant): this() {
		this.post = post
		this.text = text
		this.name = name
		this.email = email
		this.isApproved = isApproved
		this.createdAt = createdAt
	}

	fun addComment(text: String, post: Post) {
		this.text = text
		this.post = post
	}

	fun removeComment(text: String, post: Post) {
		this.text = text
		this.post = post
	}

	override fun toString(): String {
		return super.toString()
	}

}