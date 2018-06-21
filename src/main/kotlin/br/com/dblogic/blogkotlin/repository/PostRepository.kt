package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

	@Query("SELECT MAX(p.id) FROM Post p")
	fun getMaxId(): Long

}