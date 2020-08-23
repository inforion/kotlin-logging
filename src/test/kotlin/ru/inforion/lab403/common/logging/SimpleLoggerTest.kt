package ru.inforion.lab403.common.logging

import org.junit.Test


internal class SimpleLoggerTest {
    @Test
    fun test1() {
        val log = logger(FINE)

        log.warning { "First severe message..." }
    }
}