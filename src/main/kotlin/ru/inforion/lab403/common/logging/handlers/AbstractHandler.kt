package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.formatters.BasicFormatter
import ru.inforion.lab403.common.logging.common.Info
import ru.inforion.lab403.common.logging.formatters.AbstractFormatter

abstract class AbstractHandler constructor(private val formatter: AbstractFormatter = BasicFormatter()) {
    fun log(info: Info, flush: Boolean) {
        val formatted = formatter.format(info)
        publish(formatted, info)
        if (flush) flush()
    }

    abstract fun publish(line: String, info: Info)

    abstract fun flush()
}