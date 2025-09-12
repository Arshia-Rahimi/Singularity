package com.github.singularity.core.broadcast

import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.platform

const val MDNS_SERVICE_TYPE = "_singularity_sync._tcp.local."

fun getServiceName(group: HostedSyncGroup) =
    "Singularity-$platform-${getDeviceName()}-${group.hostedSyncGroupId}"
