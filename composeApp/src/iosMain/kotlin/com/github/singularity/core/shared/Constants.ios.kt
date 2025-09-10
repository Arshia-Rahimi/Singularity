package com.github.singularity.core.shared

import platform.UIKit.UIDevice

actual val canHostSyncServer = false

actual val platform = "Ios"

actual val os: String
    get() {
        val device = UIDevice.currentDevice
        val model = device.model
        val systemName = device.systemName
        val systemVersion = device.systemVersion
        return "$model - $systemName $systemVersion"
    }
