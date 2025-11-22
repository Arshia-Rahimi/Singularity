package com.github.singularity.core.datasource

import com.github.singularity.core.shared.deviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import kotlin.enums.enumEntries

fun Boolean.toLong() = if (this) 1L else 0L

fun Long.toBoolean() = this != 0L

inline fun <reified T : Enum<T>> Long.toEnum(): T = enumEntries<T>()[this.toInt()]

const val MDNS_SERVICE_TYPE = "_singularity._tcp"

fun getServiceName(group: HostedSyncGroup) =
    "${group.name}@Singularity-${deviceName}"
