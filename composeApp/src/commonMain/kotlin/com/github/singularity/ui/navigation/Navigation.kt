package com.github.singularity.ui.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.singularity.ui.designsystem.shared.ObserveForEvents
import com.github.singularity.ui.designsystem.shared.rememberCanPopBackStack
import com.github.singularity.ui.feature.connection.ConnectionScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.permissions.PermissionsScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import com.github.singularity.ui.feature.test.TestScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
	val navController = rememberNavController()

	NavigationDrawer(
		navController = navController,
	) {
		NavigationHost(navController)
	}
}

@Composable
private fun NavigationHost(
    navController: NavHostController,
) {
    NavHost(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        enterTransition = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
            )
        },
        popEnterTransition = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
            )
        },
	    exitTransition = { fadeOut() },
        navController = navController,
	    startDestination = Route.Connection,
    ) {
	    composable<Route.Connection> {
            ConnectionScreen()
        }
        composable<Route.Settings> {
            SettingsScreen()
        }
	    composable<Route.Log> {
            LogScreen()
        }
	    composable<Route.Permissions> {
		    PermissionsScreen()
	    }
	    composable<Route.Test> {
		    TestScreen()
	    }
    }

    val canPopBackStack by navController.rememberCanPopBackStack()
    LaunchedEffect(canPopBackStack) {
        AppNavigationController.setCanPopBackStack(canPopBackStack)
    }

    ObserveForEvents(AppNavigationController.popStackEvent) {
        navController.popBackStack()
    }

}
