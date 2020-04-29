package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_post_image")
data class PostImage(@Id
                     @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
                     @GenericGenerator(name = "native", strategy = "native")
                     val id: Long = 0,

                     var filename: String = "",

                     var description: String = "",

                     @Lob
                     var image: ByteArray = ByteArray(0),

                     var isCoverImage: Boolean = false,

                     @ManyToOne(fetch = FetchType.LAZY)
                     var post: Post? = Post()) {

    constructor(filename: String, description: String, image: ByteArray, isCoverImage: Boolean, post: Post): this() {
        this.filename = filename
        this.description = description
        this.image = image
        this.isCoverImage = isCoverImage
        this.post = post
    }

}