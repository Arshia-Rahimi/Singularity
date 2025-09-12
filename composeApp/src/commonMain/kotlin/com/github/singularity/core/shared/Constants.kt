package com.github.singularity.core.shared

expect val canHostSyncServer: Boolean

expect val platform: String

expect val os: String

const val DataStoreFileName = "singularity.preferences_pb"

const val SERVER_PORT = 7836

const val DISCOVER_TIMEOUT = 30_000L
