@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.logging

import ru.inforion.lab403.common.logging.misc.Colors
import ru.inforion.lab403.common.logging.misc.os
import java.util.logging.Level


// Level is made with constant to for maximum optimization
const val OFF  = Int.MAX_VALUE
const val SEVERE  = 1000
const val WARNING  = 900
const val INFO = 800
const val CONFIG = 700
const val FINE = 500
const val FINER  = 400
const val FINEST = 300
const val DEBUG = 200
const val TRACE = 100
const val ALL = Int.MIN_VALUE


/**
 * Tries to convert given [this] string to log level
 *
 * @since 0.2.0
 */
fun String.logLevel() = when (this) {
    "OFF" -> OFF
    "SEVERE" -> SEVERE
    "WARNING" -> WARNING
    "INFO" -> INFO
    "CONFIG" -> CONFIG
    "FINE" -> FINE
    "FINER" -> FINER
    "FINEST" -> FINEST
    "DEBUG" -> DEBUG
    "TRACE" -> TRACE
    "ALL" -> ALL
    else -> error("Unknown level $this")
}

/**
 * Provides compatibility with java logging [Level] class
 *
 * @since 0.2.0
 */
inline fun Level.logLevel() = when (this) {
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

/**
 * Gets the full string representation of log level
 *
 * @since 0.2.0
 */
inline val LogLevel.name get() = when (this) {
    OFF -> "OFF"
    SEVERE -> "SEVERE"
    WARNING -> "WARNING"
    INFO -> "INFO"
    CONFIG -> "CONFIG"
    FINE -> "FINE"
    FINER -> "FINER"
    FINEST -> "FINEST"
    DEBUG -> "DEBUG"
    TRACE -> "TRACE"
    ALL -> "ALL"
    else -> error("Can't get level name for $this")
}

/**
 * Gets the short string representation of log level
 *
 * @since 0.2.3
 */
inline val LogLevel.abbreviation get() = when (this) {
    OFF -> "OFF"
    SEVERE -> "SVR"
    WARNING -> "WRN"
    CONFIG -> "CFG"
    INFO -> "INF"
    FINE -> "FNE"
    FINER -> "FNR"
    FINEST -> "FST"
    DEBUG -> "DBG"
    TRACE -> "TRC"
    ALL -> "ALL"
    else -> toString()
}

/**
 * Gets the associated color of log level
 *
 * @since 0.2.3
 */
val LogLevel.color get() = if (os.windows) Colors.ANSI_NONE else when (this) {
    SEVERE -> Colors.ANSI_RED
    WARNING -> Colors.ANSI_YELLOW
    INFO -> Colors.ANSI_RESET
    CONFIG -> Colors.ANSI_GREEN
    FINE -> Colors.ANSI_PURPLE
    FINER -> Colors.ANSI_BLUE
    FINEST -> Colors.ANSI_CYAN
    DEBUG -> Colors.ANSI_BRIGHT_RED
    TRACE -> Colors.ANSI_GRAY
    OFF -> Colors.ANSI_BLACK
    else -> Colors.ANSI_WHITE
}

/**
 * Checks if [this] log level permits to emit [other] log level
 *
 * @since 0.2.0
 */
inline infix fun LogLevel.permit(other: LogLevel) = other >= this && this != OFF

/**
 * Enumeration of possible log levels in one class
 * This class added for usage in outer methods where enum is required
 *
 * @since 0.2.3
 */
enum class Levels {
    ALL, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, DEBUG, TRACE, OFF;

    val level get() = name.logLevel()

    val abbreviation get() = level.abbreviation

    val color get() = level.color
}
