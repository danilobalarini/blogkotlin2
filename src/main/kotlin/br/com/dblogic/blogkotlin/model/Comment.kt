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
				   var post: Post = Post(),
				   
				   var name: String = "",

				   var email: String = "",

				   @Column(columnDefinition = "text")
				   var text: String = "",

				   @Column(nullable = false)
				   var approved: Boolean = false): DateAudit() {

	constructor(post: Post, name: String, email: String, text: String, approved: Boolean): this() {
		this.post = post
		this.name = name
		this.email = email
		this.text = text
		this.approved = approved
	}

	constructor(post: Post, text: String, name: String, email: String, approved: Boolean, createdAt: Instant): this() {
		this.post = post
		this.text = text
		this.name = name
		this.email = email
		this.approved = approved
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