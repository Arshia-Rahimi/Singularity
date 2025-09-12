package com.github.singularity.core.shared

import android.os.Build
import com.github.singularity.core.broadcast.nsd.DiscoveredService
import com.github.singularity.core.shared.model.LocalServer
import java.util.Locale.getDefault

actual fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer, ignoreCase = true)) {
        model.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }
    } else {
        "${model.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }} $model"
    }
}

fun DiscoveredService.toServer() = LocalServer(
    ip = addresses.firstOrNull() ?: "",
    deviceName = txt["deviceName"]?.decodeToString() ?: "Unknown Device",
    deviceId = txt["deviceId"]?.decodeToString() ?: "Unknown",
    syncGroupName = txt["syncGroupName"]?.decodeToString() ?: "Unknown",
    deviceOs = txt["deviceOs"]?.decodeToString() ?: "Unknown",
    syncGroupId = txt["SyncGroupId"]?.decodeToString() ?: "Unknown",
)
