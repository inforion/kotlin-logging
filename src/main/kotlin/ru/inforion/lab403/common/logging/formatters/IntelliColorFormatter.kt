@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.extensions.stretch
import ru.inforion.lab403.common.logging.common.*
import java.text.SimpleDateFormat
import java.util.*

class IntelliColorFormatter: ColorMultilineFormatter() {

    companion object {
        var locationLength = 50
        var dateFormat = "HH:mm:ss"
    }

    private inline fun stretch(string: String, maxlen: Int) = if (string.length <= maxlen)
        string.stretch(maxlen, false)
    else {
        val stretched = string.stretch(maxlen - 3, false)
        "...$stretched"
    }

    private inline fun formatLocation(caller: StackTraceElement): String {
        // TODO: Wait while JB fix wrong regex pattern for stack trace element in console
        //   see parseStackTraceLine in KotlinExceptionFilter.kt at Kotlin repo
        val location = caller.toString()
        return stretch(location, locationLength)
    }

    private inline fun formatLevel(level: LogLevel) = when (level) {
        OFF -> "OFF"
        SEVERE -> "SVR"
        WARNING -> "WRN"
        CONFIG -> "CFG"
        INFO -> "INF"
        FINE -> "FNE"
        FINER -> "FNR"
        FINEST -> "FST"
        ALL -> "ALL"
        else -> level.toString()
    }

    private inline fun formatDate(millis: Long) = SimpleDateFormat(dateFormat).format(Date(millis))

    override fun formatFirstLine(record: Record, line: String): String {
        val location = formatLocation(record.caller)
        val level = formatLevel(record.level)
        val time = formatDate(record.millis)
        return "$time $level $location: $line"
    }
}