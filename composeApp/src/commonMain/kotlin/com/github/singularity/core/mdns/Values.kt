package com.github.singularity.core.mdns

import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.platform

expect val canHostSyncServer: Boolean

val MDNS_SERVICE_NAME = "Singularity-$platform-${getDeviceName()}"
const val MDNS_SERVICE_TYPE = "_sync_service._tcp"
