package com.github.singularity.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    object Main : Route

    @Serializable
    object Settings : Route

}
