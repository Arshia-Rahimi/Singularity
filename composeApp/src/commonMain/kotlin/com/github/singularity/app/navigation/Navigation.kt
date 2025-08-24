package com.github.singularity.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.feature.discover.DiscoverScreen
import com.github.singularity.ui.feature.main.MainScreen
import com.github.singularity.ui.feature.publish.PublishScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val viewModel = koinInject<NavigationViewModel>()
    val theme by viewModel.appTheme.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    SingularityTheme(theme) {
        NavHost(
            navController = navController,
            startDestination = Route.Main,
        ) {
            composable<Route.Main> {
                MainScreen(
                    toDiscoverScreen = { navController.navigate(Route.Discover) },
                )
            }
            composable<Route.Discover> {
                DiscoverScreen(
                    navBack = navController::popBackStack,
                )
            }
            composable<Route.Publish> {
                PublishScreen(
                    navBack = navController::popBackStack,
                )
            }
            composable<Route.Settings> {
                SettingsScreen(
                    navBack = navController::popBackStack,
                )
            }
        }
    }
}
