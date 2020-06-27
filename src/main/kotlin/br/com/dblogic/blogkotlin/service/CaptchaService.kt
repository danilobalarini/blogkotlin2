package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.recaptcha.AbstractCaptchaService
import br.com.dblogic.blogkotlin.recaptcha.GoogleResponse
import br.com.dblogic.blogkotlin.recaptcha.ReCaptchaInvalidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class CaptchaService : AbstractCaptchaService() {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Value("\${google.recaptcha.key.site}")
    lateinit var keysite: String

    @Value("\${google.recaptcha.key.secret}")
    lateinit var keysecret: String

    @Value("\${google.recaptcha.key.threshold}")
    lateinit var threshold: String

    @Throws(ReCaptchaInvalidException::class)
    fun processResponse(response: String?, action: String): GoogleResponse {

        securityCheck(response)
        LOGGER.info("passou no securityCheck")

        val verifyUri = URI.create(java.lang.String.format(RECAPTCHA_URL_TEMPLATE, keysecret, response, getClientIP()))
        LOGGER.info("verifyUri: $verifyUri")

        var googleResponse = GoogleResponse();

        try {
            googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse::class.java)!!
            LOGGER.info("googleResponse: $googleResponse")

            if (!googleResponse.success || googleResponse.action != action || googleResponse.score < threshold.toFloat()) {
                if (googleResponse.hasClientError()) {
                    reCaptchaAttemptService?.reCaptchaFailed(getClientIP())
                }
                throw ReCaptchaInvalidException("reCaptcha was not successfully validated")
            }

        } catch (rce: RestClientException) {
            //throw ReCaptchaUnavailableException("Registration unavailable at this time.  Please try again later.", rce)
            LOGGER.info(rce.toString())
        }

        reCaptchaAttemptService?.reCaptchaSucceeded(getClientIP())

        return googleResponse
    }

}