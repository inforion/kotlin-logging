package ru.inforion.lab403.common.logging

import java.util.logging.Level
import java.util.logging.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Created by Alexei Gladkikh on 23/01/17.
 */

// Return logger for Java class, if companion object fix the name
fun <T: Any> logger(forClass: Class<T>, level: Level): Logger {
    val klass = unwrapCompanionClass(forClass)
    return Logging.create(klass, level)
}

// unwrap companion class to enclosing class given a Java Class
fun <T: Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) {
        ofClass.enclosingClass
    } else {
        ofClass
    }
}

// unwrap companion class to enclosing class given a Kotlin Class
fun <T: Any> unwrapCompanionClass(ofClass: KClass<T>): KClass<*> {
    return unwrapCompanionClass(ofClass.java).kotlin
}

// Return logger for Kotlin class
fun <T: Any> logger(forClass: KClass<T>, level: Level): Logger {
    return logger(forClass.java, level)
}

// return logger from extended class (or the enclosing class)
fun <T: Any> T.logger(level: Level = Level.FINE): Logger {
    return logger(this.javaClass, level)
}

// return a lazy logger property delegate for enclosing class
fun <R : Any> R.lazyLogger(level: Level = Level.FINE): Lazy<Logger> {
    return lazy { logger(this.javaClass, level) }
}

// return a logger property delegate for enclosing class
fun <R : Any> R.injectLogger(level: Level): Lazy<Logger> {
    return lazyOf(logger(this.javaClass, level))
}

fun Logger.level(newLevel: Level) {
    this.level = newLevel
    this.handlers.forEach { it.level = newLevel }
}