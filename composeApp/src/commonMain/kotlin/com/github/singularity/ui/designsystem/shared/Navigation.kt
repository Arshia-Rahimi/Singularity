package com.github.singularity.ui.designsystem.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

val NavController.currentRoute: State<String?>
    @Composable get() {
        val currentBackStackEntry by currentBackStackEntryAsState()
        return derivedStateOf { currentBackStackEntry?.destination?.route?.split(".")?.last() }
    }

fun NavController.popToRoot() {
    val root = graph.startDestinationRoute ?: return
    popBackStack(
        route = root,
        inclusive = false,
    )
}

@Composable
fun NavController.rememberCanPopBackStack(): State<Boolean> {
    val currentEntry by currentBackStackEntryAsState()
    return remember(currentEntry) {
        derivedStateOf {
            previousBackStackEntry != null
        }
    }
}

fun <T : Any> NavController.navigateAndClearStack(route: T) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}
