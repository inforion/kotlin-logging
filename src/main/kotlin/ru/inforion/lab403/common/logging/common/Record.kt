package ru.inforion.lab403.common.logging.common

class Record constructor(
    val name: String,
    val message: String,
    val level: LogLevel,
    val millis: Long,
    val caller: StackTraceElement
) {
    val sourceMethodName: String get() = caller.methodName
    val sourceClassName: String get() = caller.className
    val sourceLineNumber: Int get() = caller.lineNumber

    val sourceFileName: String get() = caller.fileName ?: "null"
}