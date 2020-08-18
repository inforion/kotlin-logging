package ru.inforion.lab403.common.logging.common

import java.util.logging.Level

class Info constructor(
    val logger: Logger,
    val level: Level,
    val millis: Long,
    val caller: StackTraceElement
) {
    val sourceMethodName: String get() = caller.methodName
    val sourceClassName: String get() = caller.className
    val sourceLineNumber: Int get() = caller.lineNumber

    val sourceFileName: String get() = caller.fileName ?: "null"
}