package br.com.dblogic.blogkotlin.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tb_post")
data class Post(@Id
				@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_post")
				@GenericGenerator(name = "seq_post", strategy = "native")
				val id: Long = 0,

				var title: String = "default title",

				@Column(columnDefinition = "text")
				var review: String = "",

				@OneToMany(mappedBy = "post",
						   cascade = [CascadeType.ALL],
						   orphanRemoval = true)
				@JsonManagedReference
				var postImages: MutableList<PostImage> = mutableListOf<PostImage>(),

				@OneToMany(mappedBy = "post",
					cascade = [CascadeType.ALL],
					orphanRemoval = true)
				var comments: MutableList<Comment> = mutableListOf<Comment>(),

				@ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
				@JoinTable(name = "tb_post_tag",
						   joinColumns = [JoinColumn(name = "post_id")],
						   inverseJoinColumns = [JoinColumn(name = "tag_id")])
				var tags: MutableSet<Tag> = mutableSetOf<Tag>(),

				@Column(name = "parent_id", nullable = false)
				val parentId: Long = 0L,

				var isDraft: Boolean = true,

				var pageview: Long = 0L) : DateAudit() {

	constructor(title: String) : this() {
		this.title = title
	}

	constructor(title: String, review: String): this() {
		this.title = title
		this.review = review
	}

	constructor(title: String, review: String, createdAt: Instant): this() {
		this.title = title
		this.review = review
		this.createdAt = createdAt
	}

	fun addPostImage(postImage: PostImage) {
		postImages.add(postImage)
		postImage.post = this
	}

	fun removePostImage(postImage: PostImage) {
		postImages.remove(postImage)
	}

	fun addComment(comment: Comment) {
		comments.add(comment)
		comment.post = this
	}

	fun addTag(tag: Tag) {
		tags.add(tag)
		tag.posts.add(this)
	}

	fun removeTag(tag: Tag) {
		tags.remove(tag)
		tag.posts.remove(this)
	}

	override fun toString(): String {
		return "Post(id=$id, title='$title', review='$review', tags=$tags, isDraft=$isDraft, pageview=$pageview)"
	}

}