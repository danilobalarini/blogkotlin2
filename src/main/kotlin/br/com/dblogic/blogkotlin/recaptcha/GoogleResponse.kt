package br.com.dblogic.blogkotlin.recaptcha

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class GoogleResponse(@JsonProperty("success")
                          val success: Boolean = false,

                          @JsonProperty("challenge_ts")
                          val challengeTs: String? = "",

                          @JsonProperty("hostname")
                          val hostname: String? = "",

                          @JsonProperty("score")
                          val score: Float = 0f,

                          @JsonProperty("action")
                          val action: String? = "",

                          @JsonProperty("error-codes")
                          val errorCodes: Array<ErrorCode>?) {

    enum class ErrorCode {
        MissingSecret, InvalidSecret, MissingResponse, InvalidResponse, BadRequest, TimeoutOrDuplicate;

        @JsonCreator
        fun forValue(value: String): ErrorCode? {
            return errorsMap[value.toLowerCase()]
        }

        companion object {
            private val errorsMap: MutableMap<String, ErrorCode> = HashMap(4)

            init {
                errorsMap["missing-input-secret"] = MissingSecret
                errorsMap["invalid-input-secret"] = InvalidSecret
                errorsMap["missing-input-response"] = MissingResponse
                errorsMap["bad-request"] = InvalidResponse
                errorsMap["invalid-input-response"] = BadRequest
                errorsMap["timeout-or-duplicate"] = TimeoutOrDuplicate
            }
        }
    }

    @JsonIgnore
    fun hasClientError(): Boolean {
        val errors = errorCodes ?: return false
        for (error in errors) {
            when (error) {
                ErrorCode.InvalidResponse, ErrorCode.MissingResponse, ErrorCode.BadRequest -> return true
                else -> {
                }
            }
        }
        return false
    }

    override fun toString(): String {
        return "GoogleResponse{" + "success=" + success + ", challengeTs='" + challengeTs + '\'' + ", hostname='" + hostname + '\'' + ", score='" + score + '\'' + ", action='" + action + '\'' + ", errorCodes=" + Arrays.toString(errorCodes) + '}'
    }
}