package com.github.singularity.core.shared.model.http

data class PairRequest(
    val deviceName: String?,
    val deviceId: String?,
    val deviceOs: String?,
    val syncGroupId: String?,
    val syncGroupName: String?,
) {
    fun isValid() = listOf(deviceOs, deviceName, syncGroupId, syncGroupName, deviceId)
        .none { it == null }
}
