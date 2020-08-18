package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.extensions.emptyString
import ru.inforion.lab403.common.extensions.stretch
import ru.inforion.lab403.common.logging.common.Info
import ru.inforion.lab403.common.logging.common.OperatingSystem
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level

class BasicFormatter: AbstractFormatter() {

    companion object {
        const val levelLength = 7
        const val methodNameLength = 15
        const val fileNameLength = 12
        const val printLocation = true

        private val sdf = SimpleDateFormat("HH:mm:ss")
    }

    val ANSI_RESET = "\u001B[0m"
    val ANSI_BLACK = "\u001B[30m"
    val ANSI_RED = "\u001B[31m"
    val ANSI_GREEN = "\u001B[32m"
    val ANSI_YELLOW = "\u001B[33m"
    val ANSI_BLUE = "\u001B[34m"
    val ANSI_PURPLE = "\u001B[35m"
    val ANSI_CYAN = "\u001B[36m"
    val ANSI_WHITE = "\u001B[37m"

    private val colors = mapOf(
            Level.SEVERE to ANSI_RED,
            Level.WARNING to ANSI_YELLOW,
            Level.INFO to ANSI_RESET,
            Level.CONFIG to ANSI_GREEN,
            Level.FINE to ANSI_PURPLE,
            Level.FINER to ANSI_BLUE,
            Level.FINEST to ANSI_CYAN,
            Level.OFF to ANSI_BLACK)

    private val resetChar = if (!OperatingSystem.isWindows) ANSI_RESET else ""
    private fun getColor(info: Info): String = if (!OperatingSystem.isWindows) colors.getValue(info.level) else ""

    private fun getSourceClassName(name: String, rightDepth: Int): String {
        val path = name.split(".")
        if (rightDepth >= path.size)
            return name
        val result = path.slice((path.size - rightDepth)..(path.size - 1))
        return result.joinToString(".")
    }

    private fun formatFirstLine(info: Info, line: String): String {
        val color = getColor(info)
        val location = if (printLocation) {
            val source = info.sourceFileName.stretch(fileNameLength, false)
            val method = info.sourceMethodName.stretch(methodNameLength, true)
            val number = "%-4d".format(info.sourceLineNumber)
            " $source:$number $method"
        } else emptyString
        val level = "${info.level}".stretch(levelLength)
        val time = sdf.format(Date(info.millis))
        return "$color$time $level$location $line$resetChar\n"
    }

    private fun formatOtherLines(info: Info, others: List<String>) =
            others.joinToString("\n") { "${getColor(info)}$it$resetChar" }

    override fun format(message: String, info: Info): String {
        val lines = message.lines()
        return if (lines.size > 1) {
            // need to added character color for all lines
            val first = formatFirstLine(info, lines.first())
            val others = formatOtherLines(info, lines.drop(1))
            "$first$others\n"
        } else {
            formatFirstLine(info, message)
        }
    }
}