package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "tb_post")
data class Post(@Id
				@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
				@GenericGenerator(name = "native", strategy = "native")
				val id: Long = 0,

				var title: String = "default title",

				@Lob
				var text: String = "",

				@OneToMany(mappedBy = "post",
						   cascade = [CascadeType.ALL],
						   orphanRemoval = true)
				var postImages: MutableList<PostImage> = mutableListOf<PostImage>(),

				@OneToMany(mappedBy = "post",
						   cascade = [CascadeType.ALL],
						   orphanRemoval = true)
				var comments: MutableList<Comment> = mutableListOf<Comment>(),

				var isDraft: Boolean = true) : DateAudit() {

	constructor(title: String): this() {
		this.title = title
	}

	constructor(title: String, text: String): this() {
		this.title = title
		this.text = text
	}

	constructor(title: String, text: String, createdAt: Instant): this() {
		this.title = title
		this.text = text
		this.createdAt = createdAt
	}

	fun addPostImage(postImage: PostImage) {
		postImages.add(postImage)
		postImage.post = this
	}

	fun removePostImage(postImage: PostImage) {
		postImages.remove(postImage)
		postImage.post = null
	}

	fun addComment(comment: Comment) {
		comments.add(comment)
		comment.post = this
	}
	
	fun removeComment(comment: Comment) {
		comments.remove(comment)
		comment.post = null
	}

}