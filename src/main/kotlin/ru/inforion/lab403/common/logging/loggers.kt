package ru.inforion.lab403.common.logging

import ru.inforion.lab403.common.logging.dsl.PublishersArrayConfig
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.BeautifulPublisher
import java.util.logging.Level
import kotlin.reflect.full.companionObject

/**
 * Unwraps companion class to enclosing class given a Java Class
 */
internal fun <T: Any> unwrapCompanionClass(ofClass: Class<T>) = if (ofClass.enclosingClass != null &&
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) ofClass.enclosingClass else ofClass

/**
 * Returns logger for Java class, if companion object fix the name
 */
fun <T: Any> logger(forClass: Class<T>, level: LogLevel, vararg publishers: AbstractPublisher): Logger {
    val klass = unwrapCompanionClass(forClass)
    return Logger.create(klass, level, *publishers)
}

/**
 * Returns logger from extended class (or the enclosing class)
 *
 * @param level Logger level
 * @param publishers publishers list
 */
fun <T: Any> T.logger(
    level: LogLevel = FINE,
    vararg publishers: AbstractPublisher = arrayOf(BeautifulPublisher.stdout())
) = logger(javaClass, level, *publishers)

/**
 * Returns logger from extended class (or the enclosing class)
 *
 * @param level Logger level
 * @param publishers publishers configuration
 */
fun <T: Any> T.logger(
    level: LogLevel = FINE,
    publishers: PublishersArrayConfig.() -> PublishersArrayConfig
) = logger(javaClass, level, *PublishersArrayConfig().publishers().generate())

/**
 * Returns logger from extended class (or the enclosing class)
 */
@Deprecated("please use logger(level: LogLevel, ...)")
fun <T: Any> T.logger(level: Level = Level.FINE) = logger(javaClass, level.logLevel())
