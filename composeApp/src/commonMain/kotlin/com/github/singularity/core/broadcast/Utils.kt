package com.github.singularity.core.broadcast

import com.github.singularity.core.shared.deviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.platform

const val MDNS_SERVICE_TYPE = "_singularity._tcp."

fun getServiceName(group: HostedSyncGroup) =
    "${group.name}@Singularity-$platform-${deviceName}"
