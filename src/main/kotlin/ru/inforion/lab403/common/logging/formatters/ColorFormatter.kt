package ru.inforion.lab403.common.logging

import ru.inforion.lab403.common.extensions.emptyString
import ru.inforion.lab403.common.extensions.stretch
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level

/**
 * Created by batman on 12/06/16.
 */
class Formatter(val config: Config) {

    data class Config(
            val levelLength: Int,
            val classNameLength: Int,
            val methodNameLength: Int,
            val printLocation: Boolean,
            val dateFormat: SimpleDateFormat)

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

    private val names = mapOf(
            Level.SEVERE to "SEVERE",
            Level.WARNING to "WARN",
            Level.INFO to "INFO",
            Level.CONFIG to "CONFIG",
            Level.FINE to "FINE",
            Level.FINER to "FINER",
            Level.FINEST to "FINEST",
            Level.OFF to "OFF")

    private val resetChar = if (!OperatingSystem.isWindows) ANSI_RESET else ""
    private fun getColor(info: Info): String = if (!OperatingSystem.isWindows) colors.getValue(info.level) else ""

    private fun getSourceClassName(name: String, rightDepth: Int): String {
        val path = name.split(".")
        if (rightDepth >= path.size)
            return name
        val result = path.slice((path.size - rightDepth)..(path.size - 1))
        return result.joinToString(".")
    }

    private fun getSourceMethodName(name: String): String = name.split("$")[0]

    private fun formatFirstLine(info: Info, line: String): String {
        val color = getColor(info)
        val location = if (config.printLocation) {
            val methodName = getSourceMethodName(info.sourceMethodName)
            val logger = info.logger.name.stretch(config.classNameLength, false)
            val method = methodName.stretch(config.methodNameLength, true)
            " [$logger.$method]"
        } else emptyString
        val level = names[info.level]?.stretch(config.levelLength)!!
        val time = config.dateFormat.format(Date(info.millis))
        return "$color$time $level$location: $line$resetChar\n"
    }

    private fun formatOtherLines(info: Info, others: List<String>) =
            others.joinToString("\n") { "${getColor(info)}$it$resetChar" }

    fun format(message: String, info: Info): String {
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