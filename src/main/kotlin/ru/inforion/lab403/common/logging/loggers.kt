package ru.inforion.lab403.common.logging

import ru.inforion.lab403.common.logging.dsl.PublishersArrayConfig
import ru.inforion.lab403.common.logging.logger.Logger
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import ru.inforion.lab403.common.logging.publishers.BeautyPublisher
import java.util.logging.Level
import kotlin.reflect.full.companionObject

/**
 * Unwraps companion class to enclosing class given a Java Class
 */
internal fun <T: Any> unwrapCompanionClass(ofClass: Class<T>) = if (ofClass.enclosingClass != null &&
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) ofClass.enclosingClass else ofClass

/**
 * Returns logger for Java class, if companion object fix the name
 *
 * @param forClass class to create logger for
 * @param level logger level
 * @param flush flush message when publish it
 * @param publishers publishers list
 */
internal fun <T: Any> logger(
    forClass: Class<T>,
    level: LogLevel,
    flush: Boolean,
    vararg publishers: AbstractPublisher = arrayOf(BeautyPublisher.stdout())
): Logger {
    val klass = unwrapCompanionClass(forClass)
    return Logger.create(klass, level, flush, *publishers)
}

/**
 * Creates logger with specified publisher list [publishers] or get existed and
 *   returns logger from extended class (or the enclosing class)
 *
 * @param level logger level
 * @param flush flush message when publish it
 * @param publishers publishers list
 */
fun <T: Any> T.logger(
    level: LogLevel = FINE,
    flush: Boolean = true,
    vararg publishers: AbstractPublisher = arrayOf(BeautyPublisher.stdout())
) = logger(javaClass, level, flush, *publishers)

/**
 * Creates logger with configuration [configuration] or get existed and
 *   returns logger from extended class (or the enclosing class)
 *
 * @param level logger level
 * @param flush flush message when publish it
 * @param configuration publishers configuration
 */
fun <T: Any> T.logger(
    level: LogLevel = FINE,
    flush: Boolean = true,
    configuration: PublishersArrayConfig.() -> PublishersArrayConfig
) = logger(javaClass, level, flush, *PublishersArrayConfig().configuration().generate())

/**
 * Returns logger from extended class (or the enclosing class)
 */
@Deprecated("please use logger(level: LogLevel, ...)")
fun <T: Any> T.logger(level: Level = Level.FINE) = logger(javaClass, level.logLevel(), true)
