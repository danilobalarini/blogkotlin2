package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_post_image")
data class PostImage(@Id
                     @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
                     @GenericGenerator(name = "native", strategy = "native")
                     val id: Long = 0,

                     val filename: String = "",

                     val description: String = "",

                     @Lob
                     val image: ByteArray = ByteArray(0),

                     val isCoverImage: Boolean = false,

                     @ManyToOne(fetch = FetchType.LAZY)
                     @JoinColumn(name = "post_id")
                     var post: Post = Post()) {

}