package com.github.singularity.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    object Main : Route

    @Serializable
    object Discover : Route

    @Serializable
    object Broadcast : Route

    @Serializable
    object Settings : Route

}
