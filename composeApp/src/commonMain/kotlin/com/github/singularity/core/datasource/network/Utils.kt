package com.github.singularity.core.datasource.network

import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.shared.deviceName

const val MDNS_SERVICE_TYPE = "_singularity._tcp"

fun getServiceName(group: HostedSyncGroupModel) =
    "${group.name}@Singularity-${deviceName}"
