package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.Post
import br.com.dblogic.blogkotlin.model.PostImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostImageRepository : JpaRepository<PostImage, Long> {

    override fun findById(id: Long): Optional<PostImage>

    fun findByPost(post: Post): PostImage

    fun findByPostAndIsCoverImageTrue(post: Post): PostImage

}