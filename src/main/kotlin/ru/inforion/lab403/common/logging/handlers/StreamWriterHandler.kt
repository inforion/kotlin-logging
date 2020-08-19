package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.common.Info
import ru.inforion.lab403.common.logging.formatters.AbstractFormatter
import java.io.Writer

class StreamWriterHandler(private val writer: Writer, formatter: AbstractFormatter) : AbstractHandler(formatter) {
    override fun publish(message: String, info: Info) = writer.write(message)

    override fun flush() = writer.flush()
}