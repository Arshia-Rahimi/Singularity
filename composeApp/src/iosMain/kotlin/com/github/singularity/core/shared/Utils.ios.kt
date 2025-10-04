package com.github.singularity.core.shared

import platform.UIKit.UIDevice

actual val deviceName: String
    get() = UIDevice.currentDevice.name
