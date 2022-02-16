package br.com.dblogic.blogkotlin.model

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_captcha_response")
data class CaptchaResponse(@Id
                           @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_captcha_response")
                           @GenericGenerator(name = "seq_captcha_response", strategy = "native")
                           val id: Long = 0,

                           val success: Boolean = false,

                           val challengeTs: LocalDateTime = LocalDateTime.now(),

                           val hostname: String? = "",

                           val score: Float = 0f,

                           val action: String? = "",

                           @Enumerated(EnumType.STRING)
                           val eventType: CaptchaEvent = CaptchaEvent.NONE,

                           val eventId: Long = 0) : DateAudit() {
}