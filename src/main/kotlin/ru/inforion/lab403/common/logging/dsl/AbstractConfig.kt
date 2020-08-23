package ru.inforion.lab403.common.logging.dsl

fun interface AbstractConfig<T> {
    fun generate(): T
}