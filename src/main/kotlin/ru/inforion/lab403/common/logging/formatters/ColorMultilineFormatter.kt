package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.extensions.os
import ru.inforion.lab403.common.logging.*
import ru.inforion.lab403.common.logging.logger.Record

object ColorMultilineFormatter: AbstractFormatter() {

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

    private fun LogLevel.color() = if (os.windows) ANSI_NONE else when (this) {
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

    private val resetChar = if (os.windows) ANSI_NONE else ANSI_RESET

    private fun String.paint(color: String) = "${color}$this$resetChar"

    override fun format(message: String, record: Record): String {
        val lines = message.lines()
        val color = record.level.color()
        return lines.joinToString(separator = "\n") { it.paint(color) }
    }
}