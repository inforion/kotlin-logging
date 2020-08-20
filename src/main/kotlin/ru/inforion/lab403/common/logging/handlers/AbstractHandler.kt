package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.common.Record

abstract class AbstractHandler {
    abstract fun publish(record: Record)

    abstract fun flush()
}