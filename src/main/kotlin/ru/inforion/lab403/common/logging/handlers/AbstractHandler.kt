package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.formatters.ColorFormatter
import ru.inforion.lab403.common.logging.common.Info

abstract class AbstractHandler(private val formatter: ColorFormatter) {
    fun log(message: String, info: Info) {
        publish(formatter.format(message, info), info)
    }

    abstract fun publish(message: String, info: Info)

    abstract fun close()

    abstract fun flush()
}