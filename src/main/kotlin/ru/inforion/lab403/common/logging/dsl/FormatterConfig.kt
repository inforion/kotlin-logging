package ru.inforion.lab403.common.logging.dsl

import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.formatters.AbstractFormatter


class FormatterConfig : AbstractConfig<AbstractFormatter> {
    /**
     * Function called when publish record
     */
    private var onFormat: AbstractFormatter.(message: String, record: Record) -> String = { message, _ -> message }

    override fun generate() = object : AbstractFormatter {
        override fun format(message: String, record: Record): String = onFormat(message, record)
    }

    /**
     * Set the [AbstractFormatter.format] method of formatter, default - just message return from formatting
     *
     * @param function publish action
     */
    fun format(function: AbstractFormatter.(message: String, record: Record) -> String) = apply {
        onFormat = function
    }
}