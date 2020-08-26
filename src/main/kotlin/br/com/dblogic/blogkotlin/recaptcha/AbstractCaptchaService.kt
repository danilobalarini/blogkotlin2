package br.com.dblogic.blogkotlin.recaptcha

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

abstract class AbstractCaptchaService {

    @Autowired
    protected var request: HttpServletRequest? = null

    @Autowired
    protected var reCaptchaAttemptService: ReCaptchaAttemptService? = null

    val LOGGER: Logger = LoggerFactory.getLogger(AbstractCaptchaService::class.java)
    val RESPONSE_PATTERN: Pattern = Pattern.compile("[A-Za-z0-9_-]+")
    val RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s"

    fun securityCheck(response: String?) {
        LOGGER.info("Attempting to validate response {}", response)
        if (reCaptchaAttemptService?.isBlocked(getClientIP())!!) {
            LOGGER.info("ReCaptchaInvalidException")
            throw ReCaptchaInvalidException("Client exceeded maximum number of failed attempts")
        }
        if (!responseSanityCheck(response)) {
            LOGGER.info("error in responseSanityCheck")
            throw ReCaptchaInvalidException("Response contains invalid characters")
        }
    }

    fun responseSanityCheck(response: String?): Boolean {
        LOGGER.info("responseSanityCheck: " + StringUtils.hasLength(response))
        LOGGER.info("responseSanityCheck: " + RESPONSE_PATTERN.matcher(response).matches())

        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches()
    }

    fun getClientIP(): String {
        val xfHeader = request!!.getHeader("X-Forwarded-For") ?: return request!!.remoteAddr
        return xfHeader.split(",").toTypedArray()[0]
    }

}