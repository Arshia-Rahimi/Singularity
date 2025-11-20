package com.github.singularity.core.datasource.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun createClient(
	certificate: ByteArray,
	configBlock: HttpClientConfig<*>.() -> Unit = {}
): HttpClient
