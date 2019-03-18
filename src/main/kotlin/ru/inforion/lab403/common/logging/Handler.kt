package ru.inforion.lab403.common.logging

import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.StreamHandler

/**
 * Created by batman on 12/06/16.
 */
class Handler(level: Level, config: Formatter.Config) :
        StreamHandler(System.out, Formatter(config)) {

    init {
        this.level = level
    }

    override fun publish(record: LogRecord?) {
        record?.let {
            super.publish(it)
            flush()
        }
    }

    override fun close() = flush()
}