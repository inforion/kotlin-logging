package ru.inforion.lab403.common.logging

import ru.inforion.lab403.common.logging.logger.Logger

inline fun <T: Any> Logger.severe(flush: Boolean = false, message: Messenger<T>) = log(SEVERE, flush, message)

inline fun <T: Any> Logger.warning(flush: Boolean = false, message: Messenger<T>) = log(WARNING, flush, message)

inline fun <T: Any> Logger.info(flush: Boolean = false, message: Messenger<T>) = log(INFO, flush, message)

inline fun <T: Any> Logger.config(flush: Boolean = false, message: Messenger<T>) = log(CONFIG, flush, message)

inline fun <T: Any> Logger.fine(flush: Boolean = false, message: Messenger<T>) = log(FINE, flush, message)

inline fun <T: Any> Logger.finer(flush: Boolean = false, message: Messenger<T>) = log(FINER, flush, message)

inline fun <T: Any> Logger.finest(flush: Boolean = false, message: Messenger<T>) = log(FINEST, flush, message)