package ru.inforion.lab403.common.logging.misc

internal object os {
    val name = System.getProperty("os.name").toLowerCase()

    val windows = name.indexOf("win") >= 0
    val mac = name.indexOf("mac") >= 0
    val unix = name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0 || name.indexOf("aix") > 0
    val solaris = name.indexOf("sunos") >= 0

    val type = when {
        windows -> "win"
        mac -> "osx"
        unix -> "uni"
        solaris -> "sol"
        else -> "unknown"
    }
}