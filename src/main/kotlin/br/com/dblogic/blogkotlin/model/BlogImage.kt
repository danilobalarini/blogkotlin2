package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_blog_image")
data class BlogImage(@Id
                     @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
                     @GenericGenerator(name = "native", strategy = "native")
                     val id: Long = 0,

                     var filename: String = "",

                     var description: String = "",

                     @Lob
                     var image: ByteArray = ByteArray(0)) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlogImage

        if (id != other.id) return false
        if (filename != other.filename) return false
        if (description != other.description) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + filename.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}