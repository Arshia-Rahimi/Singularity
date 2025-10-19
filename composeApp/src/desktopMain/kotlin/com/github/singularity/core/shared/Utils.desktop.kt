package com.github.singularity.core.shared

import java.net.InetAddress

actual val deviceName: String
    get() = try {
        InetAddress.getLocalHost().hostName
            // remove .local suffix on macos
            .removeSuffix(".local")
    } catch (e: Exception) {
        "Unknown Device"
    }
