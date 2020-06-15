package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "tb_tag")
data class Tag(@Id
               @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tag")
               @GenericGenerator(name = "seq_tag", strategy = "native")
               val id: Int = 0,

               @NaturalId
               var name: String = "",

               @ManyToMany(mappedBy = "tags")
               var posts: MutableSet<Post> = mutableSetOf<Post>()) {

    constructor(name: String): this() {
        this.name = name
    }

}
