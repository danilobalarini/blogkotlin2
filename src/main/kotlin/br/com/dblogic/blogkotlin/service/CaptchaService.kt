package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.controller.BlogController
import br.com.dblogic.blogkotlin.model.CaptchaEvent
import br.com.dblogic.blogkotlin.model.CaptchaResponse
import br.com.dblogic.blogkotlin.recaptcha.AbstractCaptchaService
import br.com.dblogic.blogkotlin.recaptcha.GoogleResponse
import br.com.dblogic.blogkotlin.recaptcha.ReCaptchaInvalidException
import br.com.dblogic.blogkotlin.repository.CaptchaResponseRepository
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDateTime

@Service
class CaptchaService : AbstractCaptchaService() {

    private val logger = LoggerFactory.getLogger(CaptchaService::class.java)

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var captchaResponseRepository: CaptchaResponseRepository

    @Value("\${google.recaptcha.key.site}")
    lateinit var keysite: String

    @Value("\${google.recaptcha.key.secret}")
    lateinit var keysecret: String

    @Value("\${google.recaptcha.key.threshold}")
    lateinit var threshold: String

    @Throws(ReCaptchaInvalidException::class)
    fun processResponse(response: String?, captchaEvent: CaptchaEvent): GoogleResponse {

        securityCheck(response)
        LOGGER.info("passou no securityCheck")

        val verifyUri = URI.create(java.lang.String.format(RECAPTCHA_URL_TEMPLATE, keysecret, response, getClientIP()))
        LOGGER.info("verifyUri: $verifyUri")

        var googleResponse = GoogleResponse();

        try {
            googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse::class.java)!!
            LOGGER.info("googleResponse: $googleResponse")

            LOGGER.info("googleResponse.action: ${googleResponse.action}")
            LOGGER.info("captchaEvent.name: ${captchaEvent.name}")

            if (!googleResponse.success || googleResponse.action != captchaEvent.name || googleResponse.score < threshold.toFloat()) {
                if (googleResponse.hasClientError()) {
                    reCaptchaAttemptService?.reCaptchaFailed(getClientIP())
                }

                throw ReCaptchaInvalidException("reCaptcha was not successfully validated")
            }

        } catch (rce: RestClientException) {
            // TODO
            //throw ReCaptchaUnavailableException("Registration unavailable at this time.  Please try again later.", rce)
            LOGGER.info(rce.toString())
        }

        reCaptchaAttemptService?.reCaptchaSucceeded(getClientIP())

        return googleResponse
    }

    fun save(captchaResponse: String, captchaEvent: CaptchaEvent, id: Long) {
        val response = processResponse(captchaResponse, captchaEvent)
        if(StringUtils.isNotBlank(response.challengeTs)) {
            val cr = responseToCaptcha(response, id)
            captchaResponseRepository.save(cr)
        }
    }

    fun responseToCaptcha(googleResponse: GoogleResponse, id: Long) : CaptchaResponse {

        logger.info("googleResponse.challengeTs: ${googleResponse.challengeTs}")
        val challengeTs = StringUtils.replace(StringUtils.defaultString(googleResponse.challengeTs), "Z", StringUtils.EMPTY)
        logger.info("challengeTs: $challengeTs")

        val hostname = StringUtils.defaultString(googleResponse.hostname)
        val action = StringUtils.defaultString(googleResponse.action)

        return CaptchaResponse(0L,
                               googleResponse.success,
                               LocalDateTime.parse(challengeTs),
                               hostname,
                               googleResponse.score,
                               action,
                               CaptchaEvent.CONTACT,
                               id)
    }

}