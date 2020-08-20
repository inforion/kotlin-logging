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
        val info = Info(this, message, level, time, caller)
        handlers.forEach { it.log(info, flush) }
    }

    inline fun isLoggable(current: LogLevel) = current >= level && level != OFF

    inline fun <T: Any> log(level: LogLevel, flush: Boolean, message: () -> T) {
        if (!isLoggable(level)) return
        val string = message()
        doLog(level, string.toString(), flush)
    }

    inline fun <T: Any> severe(flush: Boolean = false, message: () -> T) = log(SEVERE, flush, message)

    inline fun <T: Any> warning(flush: Boolean = false, message: () -> T) = log(WARNING, flush, message)

    inline fun <T: Any> info(flush: Boolean = false, message: () -> T) = log(INFO, flush, message)

    inline fun <T: Any> config(flush: Boolean = false, message: () -> T) = log(CONFIG, flush, message)

    inline fun <T: Any> fine(flush: Boolean = false, message: () -> T) = log(FINE, flush, message)

    inline fun <T: Any> finer(flush: Boolean = false, message: () -> T) = log(FINER, flush, message)

    inline fun <T: Any> finest(flush: Boolean = false, message: () -> T) = log(FINEST, flush, message)
}