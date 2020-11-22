@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.logging.logger

import ru.inforion.lab403.common.logging.*
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import java.util.logging.Level
import kotlin.concurrent.thread

class Logger private constructor(
    val name: String,
    @JvmField var level: LogLevel,
    var flushOnPublish: Boolean = true,
    vararg publishers: AbstractPublisher
) {
    companion object {
        private const val STACK_TRACE_CALLER_INDEX = 2

        private val runtime = Runtime.getRuntime()

        /**
         * Shutdown hook to flush all loggers when program exit
         */
        private val shutdownHook = thread(false) { flush() }.also { runtime.addShutdownHook(it) }

        /**
         * All already created loggers
         */
        private val loggers = mutableMapOf<String, Logger>()

        /**
         * Levels configuration loaded from JSON-file specified by environment variable
         */
        private val levels = Config()

        /**
         * Execute given [action] for each known logger
         *
         * @param action is action to execute
         *
         * @since 0.2.3
         */
        fun forEach(action: (logger: Logger) -> Unit) = apply { loggers.values.forEach(action) }

        /**
         * Execute given [action] for each known logger
         *
         * @param action is action to execute
         *
         * @since 0.2.3
         */
        fun onCreate(action: (logger: Logger) -> Unit) = apply { callbacks.add(action) }

        private val callbacks = mutableListOf<LoggerActionCallback>()

        /**
         * Create new logger by name with specified publishers or get it (logger) if it already exist for the class
         *
         * NOTE: this function is more likely internal API, please use functions from 'loggers.kt'
         *
         * @param name name of the new logger
         * @param level level of logging message below it will not be published
         * @param publishers list of publishers
         *
         * @since 0.2.0
         */
        fun create(name: String, level: LogLevel, flush: Boolean, vararg publishers: AbstractPublisher) = loggers.getOrPut(name) {
            val actual = levels[name, level]
            Logger(name, actual, flush, *publishers).apply { callbacks.forEach { it.invoke(this) } }
        }

        /**
         * Create new logger by class with specified publishers or get it (logger) if it already exist for the class
         *
         * NOTE: this function is more likely internal API, please use functions from 'loggers.kt'
         *
         * @param klass class to use to get name of the new logger
         * @param level level of logging message below it will not be published
         * @param publishers list of publishers
         *
         * @since 0.2.0
         */
        fun <T> create(klass: Class<T>, level: LogLevel, flush: Boolean, vararg publishers: AbstractPublisher) =
            create(klass.simpleName, level, flush, *publishers)

        /**
         * Add new publisher to all loggers
         *
         * @param publisher publisher to add
         *
         * @since 0.2.0
         */
        fun addPublisher(publisher: AbstractPublisher) = loggers.values.forEach { it.addPublisher(publisher) }

        /**
         * Remove publisher from all loggers
         *
         * @param publisher publisher to remove
         *
         * @since 0.2.0
         */
        fun removePublisher(publisher: AbstractPublisher) = loggers.values.forEach { it.removePublisher(publisher) }

        /**
         * Flush all publishers of all loggers
         *
         * @since 0.2.0
         */
        fun flush() = loggers.values.forEach { it.flush() }
    }

    private val handlers = publishers.toMutableSet()

    override fun toString() = name

    /**
     * Add new publisher to logger
     *
     * @param publisher publisher to add
     *
     * @since 0.2.0
     */
    fun addPublisher(publisher: AbstractPublisher) = handlers.add(publisher)

    /**
     * Remove publisher to logger
     *
     * @param publisher publisher to remove
     *
     * @since 0.2.0
     */
    fun removePublisher(publisher: AbstractPublisher) = handlers.remove(publisher)

    /**
     * Flush all publishers of logger
     *
     * @since 0.2.0
     */
    fun flush() = handlers.forEach { it.flush() }

    @PublishedApi
    internal fun log(level: LogLevel, flush: Boolean, message: String) {
        val timestamp = System.currentTimeMillis()
        val caller = Thread.currentThread().stackTrace[STACK_TRACE_CALLER_INDEX]
        val record = Record(this, level, timestamp, caller)
        handlers.forEach {
            it.publish(message, record)
            if (flush || flushOnPublish) it.flush()
        }
    }

    /**
     * Log message using defined publishers in logger
     *
     * @param level message log level
     * @param flush force to flush the record immediately
     * @param message message supplier
     *
     * @since 0.2.0
     */
    inline fun <T:Any> log(level: LogLevel, flush: Boolean = false, message: Messenger<T>) {
        if (this.level permit level) log(level, flush, message().toString())
    }

    @Deprecated("please use log(level: LogLevel, ...)")
    inline fun <T:Any> log(level: Level, flush: Boolean = false, message: Messenger<T>) =
        log(level.logLevel(), flush, message)

    /**
     * Emits a lazy severe log [message] (score = 1000)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> severe(flush: Boolean = false, message: Messenger<T>) = log(SEVERE, flush, message)

    /**
     * Emits a lazy warning log [message] (score = 900)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> warning(flush: Boolean = false, message: Messenger<T>) = log(WARNING, flush, message)

    /**
     * Emits a lazy info log [message] (score = 800)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> info(flush: Boolean = false, message: Messenger<T>) = log(INFO, flush, message)

    /**
     * Emits a lazy config log [message] (score = 700)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> config(flush: Boolean = false, message: Messenger<T>) = log(CONFIG, flush, message)

    /**
     * Emits a lazy fine log [message] (score = 500)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> fine(flush: Boolean = false, message: Messenger<T>) = log(FINE, flush, message)

    /**
     * Emits a lazy finer log [message] (score = 400)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> finer(flush: Boolean = false, message: Messenger<T>) = log(FINER, flush, message)

    /**
     * Emits a lazy finest log [message] (score = 300)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> finest(flush: Boolean = false, message: Messenger<T>) = log(FINEST, flush, message)

    /**
     * Emits a lazy debug log [message] (score = 200)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> debug(flush: Boolean = false, message: Messenger<T>) = log(DEBUG, flush, message)

    /**
     * Emits a lazy trace log [message] (score = 100)
     *
     * @param flush if true or [flushOnPublish] is true message will be emitted immediately
     *
     * @since 0.2.0
     */
    inline fun <T: Any> trace(flush: Boolean = false, message: Messenger<T>) = log(TRACE, flush, message)
}