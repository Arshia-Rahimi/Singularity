package com.github.singularity.core.client.utils

import com.github.singularity.models.Node
import io.ktor.http.parameters

class WebSocketConnectionFailedException : Exception()

class WebSocketConnectionDroppedException : Exception()

fun Node.toFormParameters() = parameters {
    append("deviceName", deviceName)
    append("deviceId", deviceId)
    append("deviceOs", deviceOs)
}
