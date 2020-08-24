package ru.inforion.lab403.common.logging.dsl

import ru.inforion.lab403.common.logging.ALL
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.formatters.AbstractFormatter
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.BeautyPublisher
import java.io.File

class PublishersArrayConfig : AbstractConfig<Array<AbstractPublisher>> {
    private val publishersConfigs = arrayListOf<AbstractConfig<out AbstractPublisher>>()

    override fun generate() = publishersConfigs.map { it.generate() }.toTypedArray()

    private inline fun <T : AbstractPublisher> append(block: () -> AbstractConfig<T>) =
        apply { block().also { publishersConfigs.add(it) } }

    /**
     * Appends publisher configured using current DSL
     *
     * @param name name of publisher (some identifier - does nothing)
     * @param level publisher log level
     * @param function publisher configuration
     */
    fun publisher(name: String = "publisher", level: LogLevel = ALL, function: PublisherConfig.() -> Unit) = append {
        PublisherConfig(name, level).also { function(it) }
    }

    /**
     * Appends specified publisher to publishers array's configuration
     *
     * @param publisher publisher to add
     */
    fun publisher(publisher: AbstractPublisher) = append { AbstractConfig { publisher } }

    /**
     * Appends stdout formatted publisher to publishers array's configuration
     *
     * @param level stdout publisher log level
     * @param formatter stdout publisher formatter
     */
    fun stdout(level: LogLevel = ALL, formatter: AbstractFormatter = BeautyPublisher.defaultFormatter) = append {
        AbstractConfig { BeautyPublisher.stdout(level, formatter) }
    }

    /**
     * Appends stderr formatted publisher to publishers array's configuration
     *
     * @param level stderr publisher log level
     * @param formatter stderr publisher formatter
     */
    fun stderr(level: LogLevel = ALL, formatter: AbstractFormatter = BeautyPublisher.defaultFormatter) = append {
        AbstractConfig { BeautyPublisher.stderr(level, formatter) }
    }

    /**
     * Appends file publisher to publishers array's configuration
     *
     * @param file file where to publish logs
     * @param append append to existed file or rewrite file
     * @param level file publisher log level
     * @param formatter file publisher formatter
     */
    fun file(
        file: File,
        append: Boolean = false,
        level: LogLevel = ALL,
        formatter: AbstractFormatter = BeautyPublisher.defaultFormatter
    ) = append {
        AbstractConfig { BeautyPublisher.file(file, append, level, formatter) }
    }

    /**
     * Appends writer publisher to publishers array's configuration
     *
     * @param name name of publisher (some identifier - does nothing)
     * @param level writer publisher log level
     * @param formatter writer publisher formatter (messages in [writer] will be passed after formatting)
     * @param writer what to do when writing messages
     */
    fun writer(
        name: String = "writer",
        level: LogLevel = ALL,
        formatter: AbstractFormatter = BeautyPublisher.defaultFormatter,
        writer: (message: String) -> Unit
    ) = append { AbstractConfig { BeautyPublisher.new(name, level, formatter, writer) } }
}