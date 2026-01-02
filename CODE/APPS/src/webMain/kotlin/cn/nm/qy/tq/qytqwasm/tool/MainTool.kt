package cn.nm.qy.tq.qytqwasm.tool

import kotlinx.datetime.*
import kotlinx.datetime.format.DateTimeFormatBuilder
import kotlin.time.Clock
import kotlin.time.Instant

object MainTool {

    fun formtime(time: Long, form: DateTimeFormatBuilder.WithDateTime.() -> Unit): String {
        return Instant.fromEpochMilliseconds(time).toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Format(form))
    }

    fun formdate(date: String, form: DateTimeFormatBuilder.WithDateTime.() -> Unit): String {
        return runCatching {
            if (date.matches(Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}[+-]\d{2}:\d{2}"""))) {
                Instant.parse(
                    date.replace(
                        Regex("""T(\d{2}:\d{2})([+-]\d{2}:\d{2})"""), "T$1:00$2"
                    )
                )
            } else {
                Instant.parse(date)
            }.toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Format(form))
        }.getOrDefault("格式错误")
    }

    fun formhour(hour: String): Double? {
        val realtime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return runCatching {
            LocalDateTime(
                year = realtime.year, month = realtime.month, day = realtime.day, hour = hour.take(2).toInt(), minute = hour.takeLast(2).toInt()
            ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds().toDouble()
        }.getOrNull()
    }
}

/*打印日志*/
fun <LOGS> showlogs(logs: LOGS?) {
    println(logs)
}

/*打印日志*/
fun <LOGS> (LOGS).showlogs() {
    showlogs(this)
}