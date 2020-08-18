package ru.inforion.lab403.common.logging

import java.util.logging.Level

data class Info constructor(
    val logger: Logger,
    val level: Level,
    val millis: Long,
    val caller: StackTraceElement
) {
    val sourceMethodName: String get() = caller.methodName
    val sourceClassName: String get() = caller.className
    val sourceLineNumber: Int get() = caller.lineNumber
}