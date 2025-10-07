package com.github.singularity.ui.navigation

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
