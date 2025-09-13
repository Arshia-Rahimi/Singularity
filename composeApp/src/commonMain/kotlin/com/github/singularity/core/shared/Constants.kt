package com.github.singularity.core.shared

expect val canHostSyncServer: Boolean
expect val platform: String
expect val os: String

const val DATASTORE_FILENAME = "singularity.preferences_pb"
const val HTTP_SERVER_PORT = 7836
const val WEBSOCKET_SERVER_PORT = 7837
const val DISCOVER_TIMEOUT = 30_000L
