package ru.inforion.lab403.common.logging.dsl

import ru.inforion.lab403.common.logging.formatters.AbstractFormatter
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.BeautifulPublisher
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
     * @param function publisher configuration
     */
    fun publisher(name: String = "publisher", function: PublisherConfig.() -> Unit) = append {
        PublisherConfig(name).also { function(it) }
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
     * @param formatter stdout publisher formatter
     */
    fun stdout(formatter: AbstractFormatter = BeautifulPublisher.defaultFormatter) = append {
        AbstractConfig { BeautifulPublisher.stdout(formatter) }
    }

    /**
     * Appends stderr formatted publisher to publishers array's configuration
     *
     * @param formatter stderr publisher formatter
     */
    fun stderr(formatter: AbstractFormatter = BeautifulPublisher.defaultFormatter) = append {
        AbstractConfig { BeautifulPublisher.stderr(formatter) }
    }

    /**
     * Appends file publisher to publishers array's configuration
     *
     * @param file file where to publish logs
     */
    fun file(file: File, formatter: AbstractFormatter = BeautifulPublisher.defaultFormatter) = append {
        AbstractConfig { BeautifulPublisher.file(file, formatter) }
    }

    /**
     * Appends writer publisher to publishers array's configuration
     *
     * @param name name of publisher (some identifier - does nothing)
     * @param formatter writer publisher formatter (messages in [writer] will be passed after formatting)
     * @param writer what to do when writing messages
     */
    fun writer(
        name: String = "writer",
        formatter: AbstractFormatter = BeautifulPublisher.defaultFormatter,
        writer: (message: String) -> Unit
    ) = append { AbstractConfig { BeautifulPublisher.new(name, formatter, writer) } }
}