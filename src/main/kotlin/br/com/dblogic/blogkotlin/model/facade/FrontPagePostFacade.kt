package br.com.dblogic.blogkotlin.model.facade

import br.com.dblogic.blogkotlin.model.Post
import java.time.Instant

data class FrontPagePostFacade(var post: Post = Post(),
							   var commentCount: Long = 0,
							   var coverImage: String,
							   var createdAt: Instant) {

	constructor(id: Long, title: String, text: String,  commentCount: Long, coverImage: String, createdAt: Instant) : this(Post(id, title, text), commentCount, coverImage, createdAt) {
		this.post = Post(id, title, text)
		this.commentCount = commentCount
		this.coverImage = coverImage
		this.createdAt = createdAt
	}

}