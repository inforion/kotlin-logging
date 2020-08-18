package ru.inforion.lab403.common.logging.common

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.inforion.lab403.common.logging.formatters.ColorFormatter
import ru.inforion.lab403.common.logging.handlers.AbstractHandler
import ru.inforion.lab403.common.logging.handlers.StreamHandler
import ru.inforion.lab403.common.logging.logLevel
import java.io.File
import java.text.SimpleDateFormat
import java.util.logging.Level

/**
 * Created by batman on 12/06/16.
 */
object Logging {
    private const val loggingConfPathVariable = "INFORION_LOGGING_CONF_PATH"
    private const val loggingConfDebugVariable = "INFORION_LOGGING_PRINT"

    private val config = ColorFormatter.Config(
        levelLength = 6,
        classNameLength = 15,
        methodNameLength = 16,
        printLocation = true,
        dateFormat = SimpleDateFormat("HH:mm:ss")
    )

    private var loggingConfDebug = false

    private val loggingLevels = mutableMapOf<String, Level>()

    private enum class State { INIT, LOADED, NOT_SPECIFIED }

    private var state = State.INIT

    fun logLevel(name: String, default: Level): Level {
        when (state) {
            State.INIT -> {
                val loggingConfDebugString = System.getenv(loggingConfDebugVariable)
                println("$loggingConfDebugVariable: $loggingConfDebugString")
                if (loggingConfDebugString != null)
                    loggingConfDebug = loggingConfDebugString != "false"

                val loggingConfPath = System.getenv(loggingConfPathVariable)
                println("$loggingConfPathVariable: $loggingConfPath")
                if (loggingConfPath == null) {
                    state =
                        State.NOT_SPECIFIED
                    return default
                }

                val confFile = File(loggingConfPath)
                if (!confFile.isFile) {
                    if (loggingConfDebug)
                        println("Logging configuration file can't be loaded: $confFile")
                    state =
                        State.NOT_SPECIFIED
                    return default
                }

                val parser = jacksonObjectMapper().apply { configure(JsonParser.Feature.ALLOW_COMMENTS, true) }

                try {
                    val json = confFile.readText()
                    val conf = parser.readValue<Map<String, String>>(json)
                    conf.forEach { loggingLevels[it.key] = it.value.logLevel() }
                } catch (e: Exception) {
                    if (loggingConfDebug)
                        println("Can't parse log level configuration file $confFile due to $e")
                    state =
                        State.NOT_SPECIFIED
                    return default
                }
                state =
                    State.LOADED
                return logLevel(
                    name,
                    default
                )
            }
            State.LOADED -> return if (loggingConfDebug) {
                val result = loggingLevels[name]
                if (result == null) {
                    println("Using default value for $name -> $default")
                    default
                } else {
                    println("Using configured value for $name -> $result")
                    result
                }
            } else loggingLevels.getOrDefault(name, default)
            State.NOT_SPECIFIED -> {
                if (loggingConfDebug)
                    println("Conf not loaded, using default value for $name -> $default")
                return default
            }
        }
    }

    private val loggers = mutableMapOf<String, Logger>()

    fun create(name: String, level: Level) = loggers.getOrPut(name) {
        val actual = logLevel(name, level)
        val stdout = StreamHandler(System.out, ColorFormatter(config))
        Logger(name, actual, stdout)
    }

    fun <T> create(klass: Class<T>, level: Level) = create(klass.simpleName, level)

    fun addHandler(handler: AbstractHandler) = loggers.values.forEach { it.addHandler(handler) }

    fun removeHandler(handler: AbstractHandler) = loggers.values.forEach { it.removeHandler(handler) }
}