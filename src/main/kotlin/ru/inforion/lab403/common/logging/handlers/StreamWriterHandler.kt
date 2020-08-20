package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.common.Info
import ru.inforion.lab403.common.logging.formatters.AbstractFormatter
import java.io.Writer

class StreamWriterHandler(private val writer: Writer, formatter: AbstractFormatter) : AbstractHandler(formatter) {
    companion object {
        var flushOnPublish = true
    }

    override fun publish(line: String, info: Info) {
        writer.write(line)
        if (flushOnPublish) flush()
    }

    override fun flush() = writer.flush()
}