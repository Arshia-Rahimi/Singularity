package com.github.singularity.core.mdns

import com.github.singularity.core.shared.PLATFORM
import com.github.singularity.core.shared.getDeviceName

expect val canHostSyncServer: Boolean

val MDNS_SERVICE_NAME = "Singularity-$PLATFORM-${getDeviceName()}"
const val MDNS_PORT = 7836
const val MDNS_SERVICE_TYPE = "_sync_service._tcp"
