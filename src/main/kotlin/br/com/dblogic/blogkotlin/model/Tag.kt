package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_tag")
data class Tag(@Id
               @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tag")
               @GenericGenerator(name = "seq_tag", strategy = "native")
               val id: Int = 0,

               var name: String = "") {

    constructor(name: String): this() {
        this.name = name
    }

}
