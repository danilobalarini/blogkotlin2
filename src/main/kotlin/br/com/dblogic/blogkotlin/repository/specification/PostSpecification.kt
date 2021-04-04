package br.com.dblogic.blogkotlin.repository.specification

import br.com.dblogic.blogkotlin.model.Post
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.Instant
import javax.persistence.criteria.Predicate

@Component
class PostSpecification {

	fun findByTitleOrReviewOrderByCreatedAt(post: Post?): Specification<Post?>? {
		return Specification { root, query, cb ->

			val predicates = mutableListOf<Predicate>()
			val title = cb.upper(root.get<String>("title"))
			val review = cb.upper(root.get<String>("review"))
			val createdAt = root.get<Instant>("createdAt")
			val isDraft = root.get<Boolean>("isDraft")

			if(post != null) {
				if(post.title.isNotBlank()) {
					predicates.add(cb.like(title, "%${post.title.toUpperCase()}%"))
				}
				if(post.review.isNotBlank()) {
					predicates.add(cb.like(review, "%${post.review.toUpperCase()}%"))
				}
			}

			predicates.add(cb.isFalse(isDraft))

			query.orderBy(cb.desc(createdAt))
			cb.and(*predicates.toTypedArray())
		}
	}

}