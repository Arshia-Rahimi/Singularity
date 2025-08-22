package com.github.singularity.core.shared

import platform.UIKit.UIDevice

actual fun getDeviceName(): String {
    return UIDevice.currentDevice.name
}
