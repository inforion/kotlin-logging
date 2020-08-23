package ru.inforion.lab403.common.logging

import org.junit.Test
import java.util.logging.Level


internal class CompatLoggerTest {
    @Test
    fun test1() {
        val log = logger(Level.FINE)

        log.severe { "First severe message..." }
    }
}