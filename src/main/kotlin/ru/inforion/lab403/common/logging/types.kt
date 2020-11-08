package ru.inforion.lab403.common.logging

import ru.inforion.lab403.common.logging.logger.Logger

internal typealias Caller = StackTraceElement

typealias LogLevel = Int

typealias Messenger <T> = () -> T

typealias LoggerActionCallback = (logger: Logger) -> Unit