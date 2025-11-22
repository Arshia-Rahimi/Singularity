package com.github.singularity.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    object Connection : Route

	@Serializable
	object Permissions : Route

	@Serializable
    object Settings : Route

    @Serializable
    object Log : Route

	@Serializable
	object Test : Route

}
