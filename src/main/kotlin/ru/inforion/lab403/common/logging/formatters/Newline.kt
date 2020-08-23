package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.logger.Record

object Newline : AbstractFormatter {
    override fun format(message: String, record: Record) = "$message\n"
}