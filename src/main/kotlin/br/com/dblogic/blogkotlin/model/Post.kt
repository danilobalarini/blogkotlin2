package br.com.dblogic.blogkotlin.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "tb_post")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator :: class, property="@postId")
data class Post(@Id
				@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
				@GenericGenerator(name = "native", strategy = "native")
				val id: Long = 0,

				var title: String = "",

				@Lob
				var text: String = "",

				@OneToMany(mappedBy = "post",
						   cascade = [CascadeType.ALL],
						   orphanRemoval = true)
				@JsonManagedReference
				var postImages: MutableList<PostImage> = mutableListOf<PostImage>(),

				@OneToMany(mappedBy = "post",
						   cascade = [CascadeType.ALL],
						   orphanRemoval = true)
				var comments: MutableList<Comment> = mutableListOf<Comment>()) : DateAudit() {

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

	override fun toString(): String {
		return super.toString()
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Post

		if (id != other.id) return false
		if (title != other.title) return false
		if (text != other.text) return false
		if (comments != other.comments) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + text.hashCode()
		result = 31 * result + comments.hashCode()
		return result
	}

}