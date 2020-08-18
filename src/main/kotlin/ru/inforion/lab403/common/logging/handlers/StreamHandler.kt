package ru.inforion.lab403.common.logging.handlers

import ru.inforion.lab403.common.logging.formatters.ColorFormatter
import ru.inforion.lab403.common.logging.common.Info
import java.io.OutputStream

open class StreamHandler(stream: OutputStream, formatter: ColorFormatter) : AbstractHandler(formatter) {
    private val writer = stream.writer()

    override fun publish(message: String, info: Info) {
        writer.write(message)
    }

    override fun flush() {
        writer.flush()
    }

    override fun close() {
        writer.close()
    }
}