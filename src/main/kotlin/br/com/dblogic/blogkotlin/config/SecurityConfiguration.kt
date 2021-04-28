package br.com.dblogic.blogkotlin.config

import br.com.dblogic.blogkotlin.security.BlogAuthenticationSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter()  {
	
	@Autowired
	lateinit var blogAuthenticationSuccessHandler : BlogAuthenticationSuccessHandler
	
	override fun configure(http: HttpSecurity) {
		http.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN") 	// se tem role, tem que estar autenticado né
				.anyRequest().permitAll() 									// permite o resto das url's
			.and()
				.oauth2Login()
				.successHandler(blogAuthenticationSuccessHandler)
			.and()
				.logout().invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID").permitAll()
		http.csrf().disable()
		http.headers().frameOptions().disable()
	}
}