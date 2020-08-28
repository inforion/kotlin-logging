package ru.inforion.lab403.common.logging.dsl

import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.formatters.Formatter


class FormatterConfig : AbstractConfig<Formatter> {
    /**
     * Function called when publish record
     */
    private var onFormat: Formatter.(message: String, record: Record) -> String = { message, _ -> message }

    override fun generate() = object : Formatter {
        override fun format(message: String, record: Record): String = onFormat(message, record)
    }

    /**
     * Set the [Formatter.format] method of formatter, default - just message return from formatting
     *
     * @param function publish action
     */
    fun format(function: Formatter.(message: String, record: Record) -> String) = apply {
        onFormat = function
    }
}