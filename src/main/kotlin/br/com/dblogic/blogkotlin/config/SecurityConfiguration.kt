package br.com.dblogic.blogkotlin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter()  {
	
	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}
	
	override fun configure(http: HttpSecurity) {
		http.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN") 	// se tem role, tem que estar autenticado né
				.anyRequest().permitAll() 					// permite o resto das url's
			.and()
				.oauth2Login()
	}
	
	override fun configure(web: WebSecurity) {
		web.ignoring().antMatchers("/css")
		web.ignoring().antMatchers("/images")
		web.ignoring().antMatchers("/templates")
	}
	
}