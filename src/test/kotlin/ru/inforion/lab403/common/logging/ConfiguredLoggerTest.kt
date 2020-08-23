package ru.inforion.lab403.common.logging

import org.junit.Test
import ru.inforion.lab403.common.extensions.toFile
import ru.inforion.lab403.common.logging.formatters.ColorMultiline
import ru.inforion.lab403.common.logging.formatters.Informative
import ru.inforion.lab403.common.logging.formatters.Newline
import ru.inforion.lab403.common.logging.formatters.Absent
import ru.inforion.lab403.common.logging.publishers.BeautifulPublisher


internal class ConfiguredLoggerTest {
    @Test
    fun test1() {
        val log = logger(ALL) {
            publisher("stderr2", SEVERE) {
                val writer = System.err.writer()

                formatter {
                    format { message, record -> "FORMAT: [time = ${record.millis} + ${message}]" }
                }

                publish { message, _ ->
                    writer.write("publisher = $name -> this formatted message: '$message' should goes to ERROR\n")
                }

                flush { writer.flush() }
            }

            publisher("stdout1", WARNING) {
                val writer = System.out.writer()

                formatter(ColorMultiline)

                publish { message, record ->
                    writer.write("logger = ${record.logger} publisher = $name -> $message\n")
                }

                flush { writer.flush() }
            }

            stdout(CONFIG)
            stderr(INFO)

            publisher(BeautifulPublisher.stdout(FINE, Newline))

            writer("test", FINER) {
                println("printer writer -> $it")
            }

            "temp".toFile().mkdirs()

            file("temp/mew_rewrite".toFile(), false, FINER, Informative(Absent))
            file("temp/mew_append".toFile(), true, WARNING, Informative(Absent))
        }

        log.severe { "Print zero severe message" }
        log.warning { "Print the first warning message" }
        log.info { "Print the second info message" }
        log.config { "Print the third config message" }
        log.fine { "Print the forth fine message" }
        log.finer { "Print the fifth finer message" }
        log.finest { "Print the six finest message" }
    }
}