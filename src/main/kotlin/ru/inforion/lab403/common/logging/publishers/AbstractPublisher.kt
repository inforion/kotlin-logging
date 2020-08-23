package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.logger.Record

abstract class AbstractPublisher(val name: String) {
    abstract fun publish(message: String, record: Record)

    abstract fun flush()
}