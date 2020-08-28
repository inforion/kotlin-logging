package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.ALL
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.logger.Record
import ru.inforion.lab403.common.logging.formatters.Formatter
import ru.inforion.lab403.common.logging.formatters.Informative
import ru.inforion.lab403.common.logging.formatters.Absent
import ru.inforion.lab403.common.logging.permit
import java.io.File
import java.io.FileWriter
import java.io.Writer

class BeautyPublisher constructor(
    name: String,
    private val writer: Writer,
    private var level: LogLevel = ALL,
    private val formatter: Formatter = Absent
) : AbstractPublisher(name) {

    companion object {
        var defaultFormatter = Informative()

        fun stdout(level: LogLevel = ALL, formatter: Formatter = defaultFormatter) =
            BeautyPublisher("stdout", System.out.writer(), level, formatter)

        fun stderr(level: LogLevel = ALL, formatter: Formatter = defaultFormatter) =
            BeautyPublisher("stderr", System.err.writer(), level, formatter)

        fun file(
            file: File,
            append: Boolean = false,
            level: LogLevel = ALL,
            formatter: Formatter = defaultFormatter
        ) = BeautyPublisher("file", FileWriter(file, append).buffered(), level, formatter)

        fun new(
            name: String,
            level: LogLevel = ALL,
            formatter: Formatter = defaultFormatter,
            write: (message: String) -> Unit
        ): BeautyPublisher {
            val writer = object : Writer() {
                override fun close() = error("close() should not be called!")
                override fun flush() = Unit
                override fun write(cbuf: CharArray, off: Int, len: Int) = error("write(cbuf) should not be called!")
                override fun write(str: String) = write(str)
            }
            return BeautyPublisher(name, writer, level, formatter)
        }
    }

    override fun publish(message: String, record: Record) {
        if (level permit record.level) {
            val formatted = formatter.format(message, record)
            writer.write(formatted)
        }
    }

    override fun flush() = writer.flush()
}