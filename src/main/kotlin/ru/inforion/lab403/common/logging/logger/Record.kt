package ru.inforion.lab403.common.logging.logger

import ru.inforion.lab403.common.logging.Caller
import ru.inforion.lab403.common.logging.LogLevel

class Record constructor(
    val logger: Logger,
    val level: LogLevel,
    val millis: Long,
    val caller: Caller
) {
    val sourceMethodName: String get() = caller.methodName
    val sourceClassName: String get() = caller.className
    val sourceLineNumber: Int get() = caller.lineNumber

    val sourceFileName: String get() = caller.fileName ?: "null"
}