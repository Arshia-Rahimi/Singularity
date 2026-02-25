package com.github.singularity.core.shared

expect val deviceName: String
expect val platform: String
expect val os: String

const val SERVER_PORT = 7836

const val LOG_FILE = "singularity.log"

const val DISCOVER_TIMEOUT_MS = 30_000L
const val WEBSOCKET_CONNECTION_RETRY_MS = 5_000L
const val WEBSOCKET_MAX_CONNECTION_RETRY_COUNT = 5
const val PAIR_CHECK_RETRY_MS = 3_000L
