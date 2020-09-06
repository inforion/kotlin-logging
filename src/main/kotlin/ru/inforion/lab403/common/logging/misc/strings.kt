@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.logging.misc

internal inline fun String.alignLeft(maxlen: Int = length) = "%${maxlen}s".format(this)

internal inline fun String.alignRight(maxlen: Int = length) = "%-${maxlen}s".format(this)

/**
 * Slice input string from 0 to maxlen.
 * If maxlen > string.length then remain string.length (no exception thrown)
 */
internal inline fun String.stretch(maxlen: Int, alignRight: Boolean = true) = when {
    length == 0 -> alignLeft(maxlen)
    alignRight -> slice(0 until length.coerceAtMost(maxlen)).alignRight(maxlen)
    else -> slice((length - maxlen).coerceAtLeast(0) until length).alignLeft(maxlen)
}