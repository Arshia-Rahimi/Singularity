package com.github.singularity.core.datasource.presence

import com.github.singularity.core.shared.deviceName
import com.github.singularity.core.shared.model.HostedSyncGroup

const val MDNS_SERVICE_TYPE = "_singularity._tcp"

fun getServiceName(group: HostedSyncGroup) =
    "${group.name}@Singularity-${deviceName}"
