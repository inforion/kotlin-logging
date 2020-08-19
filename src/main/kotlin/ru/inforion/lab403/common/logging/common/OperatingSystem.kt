package ru.inforion.lab403.common.logging.common

internal object OperatingSystem {
    val name = System.getProperty("os.name").toLowerCase()

    val isWindows = name.indexOf("win") >= 0
    val isMac = name.indexOf("mac") >= 0
    val isUnix = name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0 || name.indexOf("aix") > 0
    val isSolaris = name.indexOf("sunos") >= 0

    val type = when {
        isWindows -> "win"
        isMac -> "osx"
        isUnix -> "uni"
        isSolaris -> "sol"
        else -> "unknown"
    }
}