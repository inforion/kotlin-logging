package ru.inforion.lab403.common.logging.common

import ru.inforion.lab403.common.logging.handlers.AbstractHandler

@Suppress("NOTHING_TO_INLINE")
class Logger(val name: String, @JvmField val level: LogLevel, vararg handlers: AbstractHandler) {
    private val handlers = handlers.toMutableSet()

    fun flush() = handlers.forEach { it.flush() }

    fun addHandler(handler: AbstractHandler) = handlers.add(handler)

    fun removeHandler(handler: AbstractHandler) = handlers.remove(handler)

    private fun millis() = System.currentTimeMillis()

    private fun caller() = Thread.currentThread().stackTrace[3]

    fun doLog(level: LogLevel, message: String, flush: Boolean) {
        val time = millis()
        val caller = caller()
        val info = Info(this, level, time, caller)
        handlers.forEach { it.log(message, info, flush) }
    }

    inline fun isLoggable(current: LogLevel) = current >= level && level != OFF

    inline fun log(level: LogLevel, flush: Boolean, message: () -> String) {
        if (!isLoggable(level)) return
        doLog(level, message(), flush)
    }

    inline fun severe(flush: Boolean = false, message: () -> String) = log(SEVERE, flush, message)

    inline fun warning(flush: Boolean = false, message: () -> String) = log(WARNING, flush, message)

    inline fun info(flush: Boolean = false, message: () -> String) = log(INFO, flush, message)

    inline fun config(flush: Boolean = false, message: () -> String) = log(CONFIG, flush, message)

    inline fun fine(flush: Boolean = false, message: () -> String) = log(FINE, flush, message)

    inline fun finer(flush: Boolean = false, message: () -> String) = log(FINER, flush, message)

    inline fun finest(flush: Boolean = false, message: () -> String) = log(FINEST, flush, message)
}