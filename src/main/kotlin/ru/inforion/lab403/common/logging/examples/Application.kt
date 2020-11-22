package ru.inforion.lab403.common.logging.examples

import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher

object Application {
    val log = logger(TRACE)

    @JvmStatic
    fun main(args: Array<String>) {
        val publisher = object : AbstractPublisher("MyPublisher") {
            override fun flush() = Unit

            override fun publish(message: String, record: Record) {
                println("${record.logger.name} -> $message")
            }
        }

        Logger.forEach { it.addPublisher(publisher) }.onCreate { it.addPublisher(publisher) }

        log.severe { "This is severe message" }
        log.warning { "This is warning message" }
        log.info { "This is info message" }
        log.config { "This is config message" }
        log.fine { "This is fine message" }
        log.finer { "This is finer message" }
        log.finest { "This is finest message" }
        log.debug { "This is debug message" }
        log.trace { "This is trace message" }
    }
}