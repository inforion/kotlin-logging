package ru.inforion.lab403.common.logging

import ru.inforion.lab403.common.logging.common.Logger
import ru.inforion.lab403.common.logging.common.Logging
import ru.inforion.lab403.common.logging.common.logLevel
import java.util.logging.Level
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

// Return logger for Java class, if companion object fix the name
fun <T: Any> logger(forClass: Class<T>, level: Level): Logger {
    val klass = unwrapCompanionClass(forClass)
    return Logging.create(klass, level.logLevel())
}

// unwrap companion class to enclosing class given a Java Class
fun <T: Any> unwrapCompanionClass(ofClass: Class<T>) =
    if (ofClass.enclosingClass != null &&
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) ofClass.enclosingClass else ofClass

// unwrap companion class to enclosing class given a Kotlin Class
fun <T: Any> unwrapCompanionClass(ofClass: KClass<T>) = unwrapCompanionClass(ofClass.java).kotlin

// Return logger for Kotlin class
fun <T: Any> logger(forClass: KClass<T>, level: Level) = logger(forClass.java, level)

// return logger from extended class (or the enclosing class)
fun <T: Any> T.logger(level: Level = Level.FINE) = logger(javaClass, level)

// return a lazy logger property delegate for enclosing class
fun <R : Any> R.lazyLogger(level: Level = Level.FINE) = lazy { logger(this.javaClass, level) }

// return a logger property delegate for enclosing class
fun <R : Any> R.injectLogger(level: Level) = lazyOf(logger(this.javaClass, level))