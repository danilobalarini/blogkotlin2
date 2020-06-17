package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "tb_post")
data class Post(@Id
				@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_post")
				@GenericGenerator(name = "seq_post", strategy = "native")
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

				@ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
				@JoinTable(name = "tb_post_tag",
						   joinColumns = [JoinColumn(name = "post_id")],
						   inverseJoinColumns = [JoinColumn(name = "tag_id")])
				val tags: MutableSet<Tag> = mutableSetOf<Tag>(),

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

	fun addTag(tag: Tag) {
		tags.add(tag)
		tag.posts.add(this)
	}

	fun removeTag(tag: Tag) {
		tags.remove(tag)
		tag.posts.remove(this)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Post

		if (id != other.id) return false
		if (title != other.title) return false
		if (text != other.text) return false
		if (postImages != other.postImages) return false
		if (comments != other.comments) return false
		if (isDraft != other.isDraft) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + text.hashCode()
		result = 31 * result + postImages.hashCode()
		result = 31 * result + comments.hashCode()
		result = 31 * result + isDraft.hashCode()
		return result
	}

	override fun toString(): String {
		return "Post(id=$id, title='$title', text='$text', postImages=$postImages, comments=$comments, isDraft=$isDraft)"
	}

}