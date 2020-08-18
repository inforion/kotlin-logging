package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.formatters.BasicFormatter
import ru.inforion.lab403.common.logging.common.Info
import java.io.OutputStream
import java.io.Writer

class WriterHandler(private val writer: Writer, formatter: BasicFormatter) : AbstractHandler(formatter) {
    override fun publish(message: String, info: Info) = writer.write(message)

    override fun flush() = writer.flush()
}