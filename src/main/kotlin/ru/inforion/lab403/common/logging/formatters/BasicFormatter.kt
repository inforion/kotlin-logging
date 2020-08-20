package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.extensions.emptyString
import ru.inforion.lab403.common.extensions.stretch
import ru.inforion.lab403.common.logging.common.*
import ru.inforion.lab403.common.logging.common.OperatingSystem
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NOTHING_TO_INLINE")
class BasicFormatter: AbstractFormatter() {

    companion object {
        const val levelLength = 7
        const val methodNameLength = 15
        const val fileNameLength = 21
        const val lineNumberLength = 4
        const val printLocation = true

        const val ANSI_RESET = "\u001B[0m"
        const val ANSI_BLACK = "\u001B[30m"
        const val ANSI_RED = "\u001B[31m"
        const val ANSI_GREEN = "\u001B[32m"
        const val ANSI_YELLOW = "\u001B[33m"
        const val ANSI_BLUE = "\u001B[34m"
        const val ANSI_PURPLE = "\u001B[35m"
        const val ANSI_CYAN = "\u001B[36m"
        const val ANSI_WHITE = "\u001B[37m"
        const val ANSI_NONE = ""

        private val sdf = SimpleDateFormat("HH:mm:ss")
    }

    private fun LogLevel.color() = if (OperatingSystem.isWindows) ANSI_NONE else when (this) {
        SEVERE -> ANSI_RED
        WARNING -> ANSI_YELLOW
        INFO -> ANSI_RESET
        CONFIG -> ANSI_GREEN
        FINE -> ANSI_PURPLE
        FINER -> ANSI_BLUE
        FINEST -> ANSI_CYAN
        OFF -> ANSI_BLACK
        else -> ANSI_WHITE
    }

    private fun getColor(level: LogLevel) = if (OperatingSystem.isWindows) ANSI_NONE else level.color()
    private val resetChar = if (OperatingSystem.isWindows) ANSI_NONE else ANSI_RESET

    private inline fun formatFileName(name: String) = name.stretch(fileNameLength, false)

    private inline fun formatMethodName(name: String): String {
        if (name.length > methodNameLength) {
            val stretched = name.stretch(methodNameLength - 2, true)
            return "$stretched.."
        }

        return name.stretch(methodNameLength, true)
    }

    private inline fun formatLineNumber(number: Int) = "%-${lineNumberLength}d".format(number)

    private fun formatFirstLine(info: Info, line: String): String {
        val color = getColor(info.level)
        val location = if (printLocation) {
            val source = formatFileName(info.sourceFileName)
            val method = formatMethodName(info.sourceMethodName)
            val number = formatLineNumber(info.sourceLineNumber)
            "[$source:$number $method]"
        } else emptyString
        val level = info.level.name.stretch(levelLength)
        val time = sdf.format(Date(info.millis))
        return "$color$time $level$location $line$resetChar\n"
    }

    private fun formatOtherLines(info: Info, others: List<String>) =
            others.joinToString("\n") { "${getColor(info.level)}$it$resetChar" }

    override fun format(info: Info): String {
        val lines = info.message.lines()
        return if (lines.size > 1) {
            // need to added character color for all lines
            val first = formatFirstLine(info, lines.first())
            val others = formatOtherLines(info, lines.drop(1))
            "$first$others\n"
        } else {
            formatFirstLine(info, info.message)
        }
    }
}