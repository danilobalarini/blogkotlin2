package br.com.dblogic.blogkotlin.recaptcha

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

abstract class AbstractCaptchaService {

    @Autowired
    protected var request: HttpServletRequest? = null

    @Autowired
    protected var reCaptchaAttemptService: ReCaptchaAttemptService? = null

    val log: Logger = LoggerFactory.getLogger(AbstractCaptchaService::class.java)
    private val responsePattern: Pattern = Pattern.compile("[A-Za-z0-9_-]+")
    val recaptchaUrlTemplate = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s"

    fun securityCheck(response: String?) {
        log.info("Attempting to validate response {}", response)
        if (reCaptchaAttemptService?.isBlocked(getClientIP())!!) {
            log.info("ReCaptchaInvalidException")
            throw ReCaptchaInvalidException("Client exceeded maximum number of failed attempts")
        }
        if (!responseSanityCheck(response)) {
            log.info("error in responseSanityCheck")
            throw ReCaptchaInvalidException("Response contains invalid characters")
        }
    }

    private fun responseSanityCheck(response: String?): Boolean {
        log.info("responseSanityCheck: " + response?.isNotBlank())
        log.info("responseSanityCheck: " + responsePattern.matcher(response).matches())

        return if (response != null) {
            response.isNotEmpty() && responsePattern.matcher(response).matches()
        } else {
            false
        }
    }

    fun getClientIP(): String {
        val xfHeader = request!!.getHeader("X-Forwarded-For") ?: return request!!.remoteAddr
        return xfHeader.split(",").toTypedArray()[0]
    }

}