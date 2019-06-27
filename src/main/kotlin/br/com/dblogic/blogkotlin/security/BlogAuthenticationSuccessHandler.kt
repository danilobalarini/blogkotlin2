package br.com.dblogic.blogkotlin.security

import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory
import org.apache.commons.lang3.StringUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.io.IOException
import javax.servlet.ServletException
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpSession

@Component
class BlogAuthenticationSuccessHandler : SavedRequestAwareAuthenticationSuccessHandler() {
		
		//@Value("#{'${blog.admin.list}'.split(',')}")
		@Value("\${blog.admin}")
		val admin: String? = null
			
		private val log = LoggerFactory.getLogger(BlogAuthenticationSuccessHandler::class.java)
			
		@Throws(ServletException::class, IOException::class)
		override fun onAuthenticationSuccess(request: HttpServletRequest,
                              				 response: HttpServletResponse,
											 authentication: Authentication) {
 			
		if(checkEmail(authentication)) {
			
			val adminRole = SimpleGrantedAuthority("ROLE_ADMIN")
			val oldAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
			val updateAuthorities = mutableListOf<GrantedAuthority>()
			updateAuthorities.add(adminRole)
			updateAuthorities.addAll(oldAuthorities)
			
			SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
																									 authentication.getCredentials(),
																									 updateAuthorities))
		}
		
		log.info("authentication.getCredentials() : " + authentication.getCredentials())
		log.info("authentication.getName()		  : " + authentication.getName())
		log.info("authentication.getPrincipal()   : " + authentication.getPrincipal())
		log.info("authentication.getDetails() 	  : " + authentication.getDetails())
		
		val session = request.getSession()
			
		for(attribute in session.getAttributeNames()) {
			log.info("attribute: " + attribute + " -> " + session.getAttribute(attribute))
		}

		super.onAuthenticationSuccess(request, response, authentication)
	}
	
	fun checkEmail(authentication: Authentication?) : Boolean {
		
		if(StringUtils.isNotBlank(authentication?.getPrincipal().toString())) {
			
			val email = extractEmailFromPrincipal(authentication?.getPrincipal().toString())
						
			if(StringUtils.equalsIgnoreCase(admin, email)) {
				return true
			}
		}
		return false
	}
	
	fun extractEmailFromPrincipal(principal : String) : String {
		
		for(data in StringUtils.split(principal, ",")) {

/*			if(StringUtils.startsWith("User Attributes:", data)) {
				
				val attributes = StringUtils.substring(data,
													   StringUtils.indexOf(data, "["),
													   StringUtils.lastIndexOf(data, "]"))
				
				for(att in StringUtils.split(attributes, ",")) {
*/					if(StringUtils.startsWith(StringUtils.trim(data.toString()), "email=")) {
						return StringUtils.substringBetween(data.toString(), "=", "]")
					}
//				}

			}
//		}
		
		return ""
	}
		
}