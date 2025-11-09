package com.github.singularity.ui.navigation

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
