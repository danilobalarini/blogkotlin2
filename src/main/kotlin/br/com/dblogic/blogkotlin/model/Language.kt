package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NaturalId
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tb_language")
data class Language(@Id
                    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_language")
                    @GenericGenerator(name = "seq_language", strategy = "native")
                    val id: Long = 0L,

                    @NaturalId
                    var description: String = "",

                    var locale: Locale = Locale("pt", "BR"))