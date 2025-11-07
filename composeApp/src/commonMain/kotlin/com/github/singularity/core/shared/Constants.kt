package com.github.singularity.core.shared

expect val canHostSyncServer: Boolean
expect val platform: String
expect val os: String

const val SERVER_PORT = 7836
const val DISCOVER_TIMEOUT = 30_000L
const val PAIR_CHECK_RETRY_DELAY = 5_000L

const val LOG_FILE = "singularity.log"
