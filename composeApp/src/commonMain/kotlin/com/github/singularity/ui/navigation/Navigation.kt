package com.github.singularity.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.feature.broadcast.BroadcastScreen
import com.github.singularity.ui.feature.discover.DiscoverScreen
import com.github.singularity.ui.feature.main.MainScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val viewModel = koinInject<NavigationViewModel>()
    val theme by viewModel.appTheme.collectAsStateWithLifecycle()

    val navController = rememberNavController()

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
            startDestination = Route.Main,
        ) {
            composable<Route.Main> {
                MainScreen(
                    toDiscoverScreen = { navController.navigate(Route.Discover) },
                    toBroadcastScreen = { navController.navigate(Route.Broadcast) },
                    toSettingsScreen = { navController.navigate(Route.Settings) },
                )
            }
            composable<Route.Discover> {
                DiscoverScreen(
                    navBack = navController::popBackStack,
                )
            }
            composable<Route.Settings> {
                SettingsScreen(
                    navBack = navController::popBackStack,
                )
            }
            if (canHostSyncServer) {
                composable<Route.Broadcast> {
                    BroadcastScreen(
                        navBack = navController::popBackStack,
                    )
                }
            }
        }
    }
}
