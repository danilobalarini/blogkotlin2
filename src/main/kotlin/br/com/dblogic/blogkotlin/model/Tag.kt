package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "tb_tag")
data class Tag(@Id
               @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tag")
               @GenericGenerator(name = "seq_tag", strategy = "native")
               val id: Long = 0,

               var name: String = "",

               @ManyToMany(mappedBy = "tags")
               var posts: MutableSet<Post> = mutableSetOf<Post>()) {

    constructor(name: String): this() {
        this.name = name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "Tag(id=$id, name='$name')"
    }

}
