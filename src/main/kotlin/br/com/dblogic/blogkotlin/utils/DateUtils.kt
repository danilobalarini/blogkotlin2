package br.com.dblogic.blogkotlin.utils

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

@Component
class DateUtils {
	
	private val logger = LoggerFactory.getLogger(DateUtils::class.java)
			
	fun toLocalDate(instant: Instant): String {

		val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
										 .withLocale(Locale.getDefault())
										 .withZone(ZoneId.systemDefault())
		
		return formatter.format(ZonedDateTime.ofInstant(instant,
														ZoneId.systemDefault()))
	}
	
	fun getRandomDateSince(days: Int): Instant {
		
		val rand = ThreadLocalRandom.current().nextInt(1, days)
		val begin = Instant.now().minus(rand.toLong(), ChronoUnit.DAYS)
		
		val hours = TimeUnit.HOURS.toMillis(ThreadLocalRandom.current().nextLong(0, 23))
		val minutes = TimeUnit.MINUTES.toMillis(ThreadLocalRandom.current().nextLong(0, 59))
		val seconds = TimeUnit.SECONDS.toMillis(ThreadLocalRandom.current().nextLong(0, 59))
		
		return begin.minus(hours, ChronoUnit.MILLIS)
					.minus(minutes, ChronoUnit.MILLIS)
					.minus(seconds, ChronoUnit.MILLIS)
	}
	
	fun plusInstantUntilNow(instant: Instant): Instant {
		
		val interval = Duration.between(instant, Instant.now()).toMillis()
		val plus = instant.plus(ThreadLocalRandom.current().nextLong(0, interval), ChronoUnit.MILLIS)
		
		return plus;
		
	}
	
}