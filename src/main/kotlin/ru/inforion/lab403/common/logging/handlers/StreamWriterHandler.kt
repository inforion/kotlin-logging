package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.common.Record
import ru.inforion.lab403.common.logging.formatters.AbstractFormatter
import ru.inforion.lab403.common.logging.formatters.IntelliColorFormatter
import java.io.Writer

class StreamWriterHandler(
    private val writer: Writer,
    private val formatter: AbstractFormatter = IntelliColorFormatter()
) : AbstractHandler() {
    companion object {
        var flushOnPublish = true
    }

    override fun publish(record: Record) {
        val formatted = formatter.format(record)
        writer.write(formatted)
        if (flushOnPublish) flush()
    }

    override fun flush() = writer.flush()
}