package br.com.dblogic.blogkotlin.recaptcha

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class ReCaptchaAttemptService {

    val maxAttempt = 4
    val attemptsCache: LoadingCache<String, Int> = CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.HOURS).build(object : CacheLoader<String, Int>() {
        override fun load(key: String): Int {
            return 0
        }
    })

    fun reCaptchaSucceeded(key: String) {
        attemptsCache.invalidate(key)
    }

    fun reCaptchaFailed(key: String) {
        var attempts: Int = attemptsCache.getUnchecked(key)
        attempts++
        attemptsCache.put(key, attempts)
    }

    fun isBlocked(key: String): Boolean {
        return attemptsCache.getUnchecked(key) >= maxAttempt
    }

}