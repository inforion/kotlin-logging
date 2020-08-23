package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.formatters.AbstractFormatter
import ru.inforion.lab403.common.logging.formatters.IntelliColorFormatter
import ru.inforion.lab403.common.logging.formatters.WithoutChange
import java.io.File
import java.io.Writer

class BeautifulPublisher constructor(
    name: String,
    private val writer: Writer,
    private val formatter: AbstractFormatter = WithoutChange
) : AbstractPublisher(name) {

    companion object {
        var defaultFormatter = IntelliColorFormatter()

        fun stdout(formatter: AbstractFormatter = defaultFormatter) =
            BeautifulPublisher("stdout", System.out.writer(), formatter)

        fun stderr(formatter: AbstractFormatter = defaultFormatter) =
            BeautifulPublisher("stderr", System.err.writer(), formatter)

        fun file(file: File, formatter: AbstractFormatter = defaultFormatter) =
            BeautifulPublisher("file", file.writer(), formatter)

        fun new(
            name: String,
            formatter: AbstractFormatter = defaultFormatter,
            write: (message: String) -> Unit
        ): BeautifulPublisher {
            val writer = object : Writer() {
                override fun close() = error("close() should not be called!")
                override fun flush() = Unit
                override fun write(cbuf: CharArray, off: Int, len: Int) = error("write(cbuf) should not be called!")
                override fun write(str: String) = write(str)
            }
            return BeautifulPublisher(name, writer, formatter)
        }
    }

    override fun publish(message: String, record: Record) {
        val formatted = formatter.format(message, record)
        writer.write(formatted)
    }

    override fun flush() = writer.flush()
}