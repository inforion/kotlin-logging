package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.common.*

open class ColorMultilineFormatter: AbstractFormatter() {

    companion object {
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

    private val resetChar = if (OperatingSystem.isWindows) ANSI_NONE else ANSI_RESET

    private fun String.paint(color: String) = "${color}$this$resetChar"

    open fun formatFirstLine(record: Record, line: String) = line

    open fun formatOtherLine(record: Record, line: String) = line

    final override fun format(record: Record): String {
        val lines = record.message.lines()
        val color = record.level.color()
        val first = formatFirstLine(record, lines.first()).paint(color)
        val others = if (lines.size > 1)
            lines.drop(1).joinToString(separator = "\n", postfix = "\n") {
                formatOtherLine(record, it).paint(color)
            } else ""
        return "$first\n$others"
    }
}