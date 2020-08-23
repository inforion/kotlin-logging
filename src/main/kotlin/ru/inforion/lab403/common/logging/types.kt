package ru.inforion.lab403.common.logging

internal typealias Caller = StackTraceElement

typealias LogLevel = Int

typealias Messenger <T> = () -> T
