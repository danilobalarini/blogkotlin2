package br.com.dblogic.blogkotlin.security

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class BlogAuthenticationSuccessHandler : SavedRequestAwareAuthenticationSuccessHandler() {

	@Value("\${blog.admin}")
	lateinit var admin: String

	private val log = LoggerFactory.getLogger(BlogAuthenticationSuccessHandler::class.java)

	@Throws(ServletException::class, IOException::class)
	override fun onAuthenticationSuccess(request: HttpServletRequest,
										 response: HttpServletResponse,
										 authentication: Authentication) {
		log.info("Autenticado!")
		log.info("checkEmail: " + checkEmail(authentication))

		if(checkEmail(authentication)) {

			val adminRole = SimpleGrantedAuthority("ROLE_ADMIN")
			val oldAuthorities = SecurityContextHolder.getContext().authentication.authorities
			val updateAuthorities = mutableListOf<GrantedAuthority>()
			updateAuthorities.add(adminRole)
			updateAuthorities.addAll(oldAuthorities)

			SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(authentication.principal,
																									authentication.credentials,
																									updateAuthorities)
		}

		log.info("authentication.getCredentials() : " + authentication.credentials)
		log.info("authentication.getName()		  : " + authentication.name)
		log.info("authentication.getPrincipal()   : " + authentication.principal)
		log.info("authentication.getDetails() 	  : " + authentication.details)

		super.onAuthenticationSuccess(request, response, authentication)
	}

	private fun checkEmail(authentication: Authentication?) : Boolean {

		if(authentication?.principal.toString().isNotBlank()) {

			val email = extractEmailFromPrincipal(authentication?.principal.toString())

			log.info("email: $email")
			log.info("admin: $admin")

			if(admin.equals(email, ignoreCase = true)) {
				return true
			}
		}
		return false
	}

	private fun extractEmailFromPrincipal(principal : String) : String {
		for(data in principal.split(",")) {
			if(data.trim().startsWith("email=")) {
				return data.substringAfter("=").substringBefore("}]")
			}
		}
		return ""
	}

}