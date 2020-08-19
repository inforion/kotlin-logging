package ru.inforion.lab403.common.logging.common

import java.util.logging.Level

typealias LogLevel = Int

const val OFF  = Int.MAX_VALUE
const val SEVERE  = 1000
const val WARNING  = 900
const val INFO = 800
const val CONFIG = 700
const val FINE = 500
const val FINER  = 400
const val FINEST = 300
const val ALL = Int.MIN_VALUE


fun String.logLevel() = when (this) {
    "OFF" -> OFF
    "SEVERE" -> SEVERE
    "WARNING" -> WARNING
    "INFO" -> INFO
    "CONFIG" -> CONFIG
    "FINE" -> FINE
    "FINER" -> FINER
    "FINEST" -> FINEST
    "ALL" -> ALL
    else -> error("Unknown level $this")
}

fun Level.logLevel() = when (this) {
    Level.OFF -> OFF
    Level.SEVERE -> SEVERE
    Level.WARNING -> WARNING
    Level.INFO -> INFO
    Level.CONFIG -> CONFIG
    Level.FINE -> FINE
    Level.FINER -> FINER
    Level.FINEST -> FINEST
    Level.ALL -> ALL
    else -> error("Can't decode log level $this")
}

val LogLevel.name get() = when (this) {
    OFF -> "OFF"
    SEVERE -> "SEVERE"
    WARNING -> "WARNING"
    INFO -> "INFO"
    CONFIG -> "CONFIG"
    FINE -> "FINE"
    FINER -> "FINER"
    FINEST -> "FINEST"
    ALL -> "ALL"
    else -> error("Can't get level name for $this")
}