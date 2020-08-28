package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.logger.Record

fun interface Formatter {
    fun format(message: String, record: Record): String
}