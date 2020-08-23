package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.logger.Record

abstract class AbstractFormatter {
    abstract fun format(message: String, record: Record): String
}