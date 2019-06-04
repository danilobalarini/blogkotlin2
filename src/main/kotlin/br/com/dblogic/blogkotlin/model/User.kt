package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import org.springframework.util.StringUtils

data class User (@Id
		    	 @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
				 @GenericGenerator(name = "native", strategy = "native")
		   		 val id: Int = 0,

				 @Column(nullable = false)
		   		 var screenUsername: String = "someuser",
				 
				 val email: String = "",
				 
				 val password: String = "") {	
}