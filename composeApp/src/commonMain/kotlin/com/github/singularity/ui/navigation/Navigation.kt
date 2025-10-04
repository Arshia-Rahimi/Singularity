package com.github.singularity.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.navigateAndClearStack
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.feature.broadcast.BroadcastScreen
import com.github.singularity.ui.feature.discover.DiscoverScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val viewModel = koinInject<NavigationViewModel>()
    val theme by viewModel.appTheme.collectAsStateWithLifecycle()
    val syncMode by viewModel.syncMode.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    LaunchedEffect(syncMode) {
        if (syncMode == SyncMode.Client) navController.navigateAndClearStack(Route.Discover)
        else navController.navigateAndClearStack(Route.Broadcast)
    }

    SingularityTheme(theme) {
        NavHost(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500),
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500),
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(500),
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500),
                )
            },
            navController = navController,
            startDestination = Route.Discover,
        ) {
            composable<Route.Discover> {
                DiscoverScreen(
                    toSettingsScreen = { navController.navigate(Route.Settings) },
                )
            }
            composable<Route.Settings> {
                SettingsScreen(
                    navBack = navController::popBackStack,
                    toLogScreen = { navController.navigate(Route.Log) },
                )
            }
            composable<Route.Log> {
                LogScreen(
                    navBack = navController::popBackStack,
                )
            }
            if (canHostSyncServer) {
                composable<Route.Broadcast> {
                    BroadcastScreen(
                        toSettingsScreen = { navController.navigate(Route.Settings) },
                    )
                }
            }
        }
    }
}
