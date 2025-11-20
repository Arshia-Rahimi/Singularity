package com.github.singularity.core.datasource.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

actual fun createClient(
	certificate: ByteArray,
	configBlock: HttpClientConfig<*>.() -> Unit,
) = HttpClient(CIO) {
	val certFactory = CertificateFactory.getInstance("X.509")
	val cert = certFactory.generateCertificate(certificate.inputStream())

	val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
		load(null)
		setCertificateEntry("myserver", cert)
	}

	val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
	tmf.init(keyStore)
	val trustManager = tmf.trustManagers.first { it is X509TrustManager } as X509TrustManager

	engine {
		https {
			this.trustManager = trustManager
		}
	}

	configBlock()

}
