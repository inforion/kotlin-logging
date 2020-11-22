package ru.inforion.lab403.common.logging

import org.junit.Test
import ru.inforion.lab403.common.logging.formatters.ColorMultiline
import ru.inforion.lab403.common.logging.formatters.Informative
import ru.inforion.lab403.common.logging.formatters.Newline
import ru.inforion.lab403.common.logging.formatters.Absent
import ru.inforion.lab403.common.logging.publishers.BeautyPublisher
import java.io.File


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

            publisher(BeautyPublisher.stdout(FINE, Newline))

            writer("test", FINER) {
                println("printer writer -> $it")
            }

            File("temp").mkdirs()

            file(File("temp/mew_rewrite"), false, FINER, Informative(Absent))
            file(File("temp/mew_append"), true, WARNING, Informative(Absent))

            stdout(ALL)
        }

        log.severe { "Print zero severe message" }
        log.warning { "Print the first warning message" }
        log.info { "Print the second info message" }
        log.config { "Print the third config message" }
        log.fine { "Print the forth fine message" }
        log.finer { "Print the fifth finer message" }
        log.finest { "Print the six finest message" }
        log.debug { "Print the 7 debug message" }
        log.trace { "Print the 8 trace message" }
    }
}