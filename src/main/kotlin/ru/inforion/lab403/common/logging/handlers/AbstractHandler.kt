package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.formatters.BasicFormatter
import ru.inforion.lab403.common.logging.common.Info

abstract class AbstractHandler(private val formatter: BasicFormatter) {
    fun log(message: String, info: Info) {
        val formatted = formatter.format(message, info)
        publish(formatted, info)
    }

    abstract fun publish(message: String, info: Info)

    abstract fun flush()
}