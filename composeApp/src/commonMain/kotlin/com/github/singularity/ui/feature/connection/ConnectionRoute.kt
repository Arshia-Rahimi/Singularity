package com.github.singularity.ui.feature.connection

import kotlinx.serialization.Serializable

@Serializable
sealed interface ConnectionRoute {

	@Serializable
	data object Server : ConnectionRoute

	@Serializable
	data object Client : ConnectionRoute

}