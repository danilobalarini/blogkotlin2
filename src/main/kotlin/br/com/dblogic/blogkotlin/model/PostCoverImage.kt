package br.com.dblogic.blogkotlin.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.MapsId
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.GenerationType
import javax.persistence.FetchType

@Entity
@Table(name = "tb_post_cover_image")
data class PostCoverImage(@Id
				          val id: Long = 0,

				          var name: String = "",

				          @Lob
                          var coverImage: ByteArray = ByteArray(0), 
                          
                          @OneToOne(fetch = FetchType.LAZY)
                          @MapsId
                          val post: Post = Post()) : DateAudit() {

}