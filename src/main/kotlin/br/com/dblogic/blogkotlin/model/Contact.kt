package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_contact")
data class Contact(@Id
                   @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_contact")
                   @GenericGenerator(name = "seq_contact", strategy = "native")
                   val id: Long = 0,

                   @Column(nullable = false)
                   val name: String = "",

                   @Column(nullable = false)
                   val email: String = "",

                   @Column(columnDefinition = "text")
                   val message: String = "",

                   @Column(nullable = false)
                   var checked: Boolean = false) : DateAudit() {

}