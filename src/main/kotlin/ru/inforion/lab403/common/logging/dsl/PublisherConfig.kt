package ru.inforion.lab403.common.logging.dsl

import ru.inforion.lab403.common.logging.ALL
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.formatters.AbstractFormatter
import ru.inforion.lab403.common.logging.permit
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher


class PublisherConfig(val name: String, val level: LogLevel = ALL) : AbstractConfig<AbstractPublisher> {
    /**
     * Function called when publish record
     */
    private var onPublish: AbstractPublisher.(message: String, record: Record) -> Unit = { _, _ -> }

    /**
     * Function called when flush publisher
     */
    private var onFlush: AbstractPublisher.() -> Unit = {

    }

    private var formatterConfig: AbstractConfig<AbstractFormatter>? = null

    override fun generate() = object : AbstractPublisher(name) {
        private val formatter = formatterConfig?.generate()

        override fun publish(message: String, record: Record) {
            if (level permit record.level) {
                val formatted = formatter?.format(message, record) ?: message
                onPublish(formatted, record)
            }
        }

        override fun flush() = onFlush()
    }

    /**
     * Set the formatter of publisher, default - no formatting
     *
     * @param function formatter configuration
     */
    fun formatter(function: FormatterConfig.() -> Unit) {
        formatterConfig = FormatterConfig().also { function(it) }
    }

    /**
     * Set the formatter of publisher, default - no formatting
     *
     * @param formatter formatter to set
     */
    fun formatter(formatter: AbstractFormatter) {
        formatterConfig = AbstractConfig { formatter }
    }

    /**
     * Set the [AbstractPublisher.publish] method of publisher, default - nothing to be done when publish
     *
     * NOTE: All formatting should done in formatter (formatter called first then publish)
     *
     * @param function publish action
     */
    fun publish(function: AbstractPublisher.(message: String, record: Record) -> Unit) = apply {
        onPublish = function
    }

    /**
     * Set the [AbstractPublisher.flush] method of publisher, default - nothing to be done when flush
     *
     * @param function flush action
     */
    fun flush(function: AbstractPublisher.() -> Unit) = apply { onFlush = function }
}