package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.logging.common.Info

abstract class AbstractFormatter {
    abstract fun format(message: String, info: Info): String
}