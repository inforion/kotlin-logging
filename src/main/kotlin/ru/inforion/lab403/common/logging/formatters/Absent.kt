package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.logger.Record

object Absent : Formatter {
    override fun format(message: String, record: Record) = message
}