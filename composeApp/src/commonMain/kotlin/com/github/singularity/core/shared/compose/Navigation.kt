package com.github.singularity.core.shared.compose

import androidx.navigation.NavController

fun NavController.getCurrentRouteClassName() =
    currentBackStackEntry?.destination?.route?.split(".")?.last()

fun NavController.popToRoot() {
    val root = graph.startDestinationRoute ?: return
    popBackStack(
        route = root,
        inclusive = false,
    )
}

fun <T : Any> NavController.navigateAndClearStack(route: T) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}
