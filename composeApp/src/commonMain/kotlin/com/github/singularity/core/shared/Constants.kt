package com.github.singularity.core.shared

import kotlin.uuid.ExperimentalUuidApi

expect val PLATFORM: String

@OptIn(ExperimentalUuidApi::class)
val MDNS_SERVICE_NAME = "Singularity-$PLATFORM-${getDeviceName()}"
const val MDNS_PORT = 7836
const val MDNS_SERVICE_TYPE = "_sync_service._tcp" 

const val DataStoreFileName = "singularity.preferences_pb"
