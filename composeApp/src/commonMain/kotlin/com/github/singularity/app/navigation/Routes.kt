package com.github.singularity.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    object Home : Route

    @Serializable
    object Settings : Route

    @Serializable
    object Log : Route

}
