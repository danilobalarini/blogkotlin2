package br.com.dblogic.blogkotlin.repository.specification

import br.com.dblogic.blogkotlin.model.Post
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.Instant
import javax.persistence.criteria.CriteriaUpdate
import javax.persistence.criteria.Order
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Component
class PostSpecification {

	fun sumPageView(post: Post) {
//		return Specification { root, query, cb ->
//			val predicates = mutableListOf<Predicate>()
//
//			val pageviews = post.pageview++
//
////			val criteriaUpdate: CriteriaUpdate<Post> = cb.createCriteriaUpdate(Post::class.java)
//
//			cb.createCriteriaUpdate(Post::class.java).set("pageviews", "1")
//													 .where(cb.equal(root.get<Long>("id"), post.id))
//
//
////			cb.createCriteriaUpdate(Post::class.java).set(root.get<Long>("pageviews"), "1")
////													 .where(cb.equal(root.get<Long>("id"), post.id))
//
////			val employeeRoot: Root<Employee> = criteriaUpdate.from(Employee::class.java)
////			criteriaUpdate.set(employeeRoot.get(Employee_.salary),
////					criteriaBuilder.sum(employeeRoot.get(Employee_.salary), 500.0))
////					.where(criteriaBuilder.equal(employeeRoot.get(Employee_.DEPT), "IT"))
//
//
//		}
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