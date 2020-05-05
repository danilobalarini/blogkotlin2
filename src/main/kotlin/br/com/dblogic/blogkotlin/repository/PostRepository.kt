package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.facade.FrontPagePostFacade
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>, PagingAndSortingRepository<Post, Long> {

	override fun findById(id: Long): Optional<Post>

	override fun deleteById(id: Long)

	fun findTop6ByOrderByCreatedAtDesc(): List<Post>

	@Query(" SELECT new br.com.dblogic.blogkotlin.model.facade.FrontPagePostFacade(p.id, " +
				 "																		 p.title, " +
				 "																		 p.text, " +
				 "																		(SELECT count(1) " +
				 "							    								   		 FROM Comment c " +
				 "          													    	 WHERE c.post = p.id), " +
				 " 																		 i.filename," +
				 " 																		 p.createdAt)" +
				 " FROM Post p, PostImage i " +
				 " WHERE p.id = i.post " +
				 " AND i.isCoverImage = true ")
	fun findAllCustomBy(paging: Pageable): List<FrontPagePostFacade>
}
