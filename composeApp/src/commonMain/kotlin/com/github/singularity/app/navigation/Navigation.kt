package com.github.singularity.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val viewModel = koinInject<NavigationViewModel>()
    val theme by viewModel.appTheme.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    SingularityTheme(theme) {
        NavHost(
            navController = navController,
            startDestination = MainRoute,
        ) {

        }
    }
}
