package br.com.dblogic.blogkotlin.repository.specification

import br.com.dblogic.blogkotlin.model.Post
import org.apache.commons.lang3.StringUtils
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.Instant
import javax.persistence.criteria.Order
import javax.persistence.criteria.Predicate


@Component
class PostSpecification {

	fun findByTitleOrReviewOrderByCreatedAt(post: Post?): Specification<Post?>? {
		return Specification { root, query, cb ->

			val predicates = mutableListOf<Predicate>()
			val title = root.get<String>("title")
			val review = root.get<String>("review")
			val createdAt = root.get<Instant>("createdAt")

			if(post != null) {
				if(StringUtils.isNotBlank(post.title)) {
					predicates.add(cb.like(title, "%${post.title}%"));
				}

				if(StringUtils.isNotBlank(post.review)) {
					predicates.add(cb.like(review, "%${post.review}%"));
				}
			}
			query.orderBy(cb.desc(createdAt))
			cb.or(*predicates.toTypedArray())
		}
	}

	fun frontPage2(): Specification<Post> {
		return Specification { root, query, cb ->

			val predicates = mutableListOf<Predicate>()
			val createdAt = root.get<Instant>("createdAt")
			val orderList = mutableListOf<Order>()

			orderList.add(cb.desc(createdAt))

			query.orderBy(orderList)

			// TODO criar essa query
			//SELECT * 
			//FROM tb_post
			//ORDER BY "createdAt"
			//LIMIT 6;
			// http://bit.ly/2MfuXS6

			cb.and(*predicates.toTypedArray())
		}
		
	}
	
}