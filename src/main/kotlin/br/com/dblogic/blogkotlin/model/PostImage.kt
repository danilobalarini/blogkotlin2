package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "tb_post_image")
data class PostImage(@Id
                     @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_post_image")
                     @GenericGenerator(name = "seq_post_image", strategy = "native")
                     val id: Long = 0,

                     var filename: String = "",

                     var description: String = "",

                     @Lob
                     @Type(type = "org.hibernate.type.ImageType")
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