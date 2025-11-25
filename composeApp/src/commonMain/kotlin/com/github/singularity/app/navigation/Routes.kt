package com.github.singularity.app.navigation

import kotlinx.serialization.Serializable

interface Route {

    @Serializable
    object Connection : Route

	@Serializable
	object Plugins : Route

	@Serializable
    object Settings : Route

    @Serializable
    object Log : Route

	@Serializable
	object Test : Route

}
