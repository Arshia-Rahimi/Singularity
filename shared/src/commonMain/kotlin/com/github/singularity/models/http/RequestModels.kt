package com.github.singularity.models.http

data class PairRequestModel(
    val deviceName: String?,
    val deviceId: String?,
    val deviceOs: String?,
    val syncGroupId: String?,
    val syncGroupName: String?,
) {
    fun isValid() = listOf(deviceOs, deviceName, syncGroupId, syncGroupName, deviceId)
        .none { it == null }
}
