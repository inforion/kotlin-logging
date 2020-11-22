package ru.inforion.lab403.common.logging.logger

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.inforion.lab403.common.logging.Messenger
import java.io.File


class Config {
    companion object {
        private val parser = jacksonObjectMapper().apply { configure(JsonParser.Feature.ALLOW_COMMENTS, true) }

        const val confPathVariable = "INFORION_LOGGING_CONF_PATH"
        const val confDebugVariable = "INFORION_LOGGING_PRINT"
    }

    private inline fun <T> info(message: Messenger<T>) = println(message().toString())

    private inline fun <T> debug(message: Messenger<T>) {
        if (isDebugEnabled) info(message)
    }

    private fun env(name: String): String? = System.getenv(name).also {
        if (it != null) info { "$name: $it" }
    }

    private val isDebugEnabled by lazy {
        env(confDebugVariable)?.trim() == "true"
    }

    private val configurations by lazy {
        val path = env(confPathVariable)
        val file = if (path != null) File(path) else null
        if (file?.isFile != true) debug { "Logging configuration file can't be loaded: $file" }
        file
    }

    private fun String.parseJsonLogLevels() = parser.readValue<Map<String, Any>>(this)

    private val config by lazy {
        val file = configurations

        if (file == null) emptyMap() else
            runCatching {
                file.readText().parseJsonLogLevels()
            }.onSuccess {
                debug { "Successfully loading log levels configuration file '$file'" }
            }.onFailure { error ->
                debug { "Can't parse log levels configuration file '$file' due to $error" }
            }.getOrDefault(emptyMap())
    }

    operator fun <T> get(name: String, default: T): T {
        val result = config[name]
        return if (result == null) {
            debug { "Using default value for $name -> $default" }
            default
        } else {
            debug { "Using configured value for $name -> $result" }
            @Suppress("UNCHECKED_CAST")
            result as T
        }
    }
}