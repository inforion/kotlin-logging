package ru.inforion.lab403.common.logging.common

import ru.inforion.lab403.common.logging.handlers.AbstractHandler
import java.util.logging.Level

@Suppress("NOTHING_TO_INLINE")
class Logger(val name: String, @JvmField val level: LogLevel, vararg handlers: AbstractHandler) {
    companion object {
        const val STACK_TRACE_CALLER_INDEX = 3
    }

    private val handlers = handlers.toMutableSet()

    fun flush() = handlers.forEach { it.flush() }

    fun addHandler(handler: AbstractHandler) = handlers.add(handler)

    fun removeHandler(handler: AbstractHandler) = handlers.remove(handler)

    private fun millis() = System.currentTimeMillis()

    private fun caller() = Thread.currentThread().stackTrace[STACK_TRACE_CALLER_INDEX]

    inline fun isLoggable(current: LogLevel) = current >= level && level != OFF

    @PublishedApi
    internal fun log(level: LogLevel, flush: Boolean, message: String) {
        val time = millis()
        val caller = caller()
        val record = Record(name, message, level, time, caller)
        handlers.forEach {
            it.publish(record)
            if (flush) it.flush()
        }
    }

    inline fun <T:Any> log(level: LogLevel, flush: Boolean = false, message: Messenger<T>) {
        if (!isLoggable(level)) return
        log(level, flush, message().toString())
    }

    // compat
    inline fun <T:Any> log(level: Level, flush: Boolean = false, message: Messenger<T>) =
        log(level.logLevel(), flush, message)

    inline fun <T: Any> severe(flush: Boolean = false, message: Messenger<T>) = log(SEVERE, flush, message)

    inline fun <T: Any> warning(flush: Boolean = false, message: Messenger<T>) = log(WARNING, flush, message)

    inline fun <T: Any> info(flush: Boolean = false, message: Messenger<T>) = log(INFO, flush, message)

    inline fun <T: Any> config(flush: Boolean = false, message: Messenger<T>) = log(CONFIG, flush, message)

    inline fun <T: Any> fine(flush: Boolean = false, message: Messenger<T>) = log(FINE, flush, message)

    inline fun <T: Any> finer(flush: Boolean = false, message: Messenger<T>) = log(FINER, flush, message)

    inline fun <T: Any> finest(flush: Boolean = false, message: Messenger<T>) = log(FINEST, flush, message)
}