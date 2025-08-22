package com.github.singularity.core.shared

import java.net.InetAddress

actual fun getDeviceName(): String {
    return try {
        InetAddress.getLocalHost().hostName
    } catch (e: Exception) {
        "Unknown Device"
    }
}
