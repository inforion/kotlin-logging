package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.common.Record

abstract class AbstractFormatter {
    abstract fun format(record: Record): String
}