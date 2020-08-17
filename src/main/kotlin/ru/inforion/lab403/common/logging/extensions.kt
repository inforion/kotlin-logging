package ru.inforion.lab403.common.logging

import java.util.logging.Level.*

fun String.logLevel() = when (this) {
    OFF.name -> OFF
    SEVERE.name -> SEVERE
    WARNING.name -> WARNING
    INFO.name -> INFO
    CONFIG.name -> CONFIG
    FINE.name -> FINE
    FINER.name -> FINER
    FINEST.name -> FINEST
    ALL.name -> ALL
    else -> error("Unknown level $this")
}