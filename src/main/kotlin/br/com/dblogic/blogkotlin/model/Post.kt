package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.web.multipart.MultipartFile
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
data class Post(@Id
				@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
				@GenericGenerator(name = "native", strategy = "native")
				val id: Long = 0,

				var title: String = "",

				@Lob()
				var coverImage: ByteArray = ByteArray(0),

				@Column
				var mime: String? = "",

				@Lob
				var text: String = "",

				@OneToMany(mappedBy = "post",
						   cascade = [CascadeType.ALL],
						   orphanRemoval = true)
				var comments: MutableList<Comment> = mutableListOf<Comment>()) : DateAudit() {

	constructor(title: String, text: String): this() {
		this.title = title
		this.text = text
	}

	constructor(title: String, coverImage: ByteArray): this() {
		this.title = title
		this.coverImage = coverImage
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

	override fun toString(): String {
		return super.toString()
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Post

		if (id != other.id) return false
		if (title != other.title) return false
		if (!coverImage.contentEquals(other.coverImage)) return false
		if (text != other.text) return false
		if (comments != other.comments) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + coverImage.contentHashCode()
		result = 31 * result + text.hashCode()
		result = 31 * result + comments.hashCode()
		return result
	}

}