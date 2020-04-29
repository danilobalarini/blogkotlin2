package br.com.dblogic.blogkotlin.model

import javax.persistence.*

@Entity
@Table(name = "tb_post_cover_image")
data class PostCoverImage(@Id
				          val id: Long = 0,

                          var filename: String = "",

				          @Lob
                          var coverImage: ByteArray = ByteArray(0), 

                          @OneToOne(fetch = FetchType.LAZY)
                          @MapsId
                          var post: Post = Post()) : DateAudit() {

    constructor(filename: String, filepath: String, coverImage: ByteArray, post: Post): this() {
        this.coverImage = coverImage
        this.filename = filename
        this.post = post
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostCoverImage

        if (id != other.id) return false
        if (filename != other.filename) return false
        if (!coverImage.contentEquals(other.coverImage)) return false
        if (post != other.post) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + filename.hashCode()
        result = 31 * result + coverImage.contentHashCode()
        result = 31 * result + post.hashCode()
        return result
    }

}