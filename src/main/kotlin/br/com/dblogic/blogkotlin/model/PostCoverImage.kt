package br.com.dblogic.blogkotlin.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.MapsId
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.FetchType
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "tb_post_cover_image")
data class PostCoverImage(@Id
				          val id: Long = 0,

				          var name: String = "",

				          @Lob
                          var coverImage: ByteArray = ByteArray(0), 
                          
                          @OneToOne(fetch = FetchType.LAZY)
                          @MapsId
                          var post: Post = Post()) : DateAudit() {

    constructor(name: String, coverImage: ByteArray, post: Post): this() {
        this.name = name
        this.coverImage = coverImage
        this.post = post
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostCoverImage

        if (id != other.id) return false
        if (name != other.name) return false
        if (!coverImage.contentEquals(other.coverImage)) return false
        if (post != other.post) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + coverImage.contentHashCode()
        result = 31 * result + post.hashCode()
        return result
    }

}