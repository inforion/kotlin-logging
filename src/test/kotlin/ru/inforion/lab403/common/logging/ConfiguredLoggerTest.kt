package ru.inforion.lab403.common.logging

import org.junit.Test
import ru.inforion.lab403.common.extensions.mkdirs
import ru.inforion.lab403.common.extensions.toFile
import ru.inforion.lab403.common.logging.formatters.ColorMultilineFormatter
import ru.inforion.lab403.common.logging.formatters.IntelliColorFormatter
import ru.inforion.lab403.common.logging.formatters.WithoutChange


internal class ConfiguredLoggerTest {
    @Test
    fun test1() {
        val log = logger(ALL) {
            publisher("stderr2") {
                val writer = System.err.writer()

                formatter {
                    format { message, record -> "FORMAT: [time = ${record.millis} + ${message}]" }
                }

                publish { message, _ ->
                    writer.write("publisher = $name -> this formatted message: '$message' should goes to ERROR\n")
                }

                flush { writer.flush() }
            }

            publisher("stdout1") {
                val writer = System.out.writer()

                formatter(ColorMultilineFormatter)

                publish { message, record ->
                    writer.write("logger = ${record.logger} publisher = $name -> $message\n")
                }

                flush { writer.flush() }
            }

            stdout()

            stderr()

            file("temp/mew".toFile().also { it.parent.mkdirs() }, IntelliColorFormatter(WithoutChange))

            writer("test") {

            }
        }

        log.severe { "Print zero severe message" }
        println()

        log.warning { "Print the first warning message" }
        println()

        log.info { "Print the second info message" }
        println()

        log.config { "Print the third config message" }
        println()

        log.fine { "Print the forth finer message" }
        println()

        log.finer { "Print the fifth finer message" }
        println()

        log.finest { "Print the six finest message" }
        println()
    }
}