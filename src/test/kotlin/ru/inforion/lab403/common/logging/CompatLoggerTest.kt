package ru.inforion.lab403.common.logging

import org.junit.Test
import ru.inforion.lab403.common.extensions.mkdirs
import ru.inforion.lab403.common.extensions.toFile
import ru.inforion.lab403.common.logging.formatters.ColorMultilineFormatter
import ru.inforion.lab403.common.logging.formatters.IntelliColorFormatter
import ru.inforion.lab403.common.logging.formatters.WithoutChange
import java.util.logging.Level


internal class CompatLoggerTest {
    @Test
    fun test1() {
        val log = logger(Level.FINE)

        log.severe { "First severe message..." }
    }
}