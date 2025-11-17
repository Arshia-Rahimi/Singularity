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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.singularity.app.MainUiState
import com.github.singularity.ui.feature.connection.connectionNavigation
import com.github.singularity.ui.feature.log.logNavigation
import com.github.singularity.ui.feature.permissions.permissionsNavigation
import com.github.singularity.ui.feature.settings.settingsNavigation
import com.github.singularity.ui.feature.test.TestScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
	uiState: MainUiState,
	toggleSyncMode: () -> Unit,
) {
	val navController = rememberNavController()

	NavigationDrawer(
		navController = navController,
		uiState = uiState,
		toggleSyncMode = toggleSyncMode,
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
		connectionNavigation()
		settingsNavigation()
		logNavigation()
		permissionsNavigation()

		//
		composable<Route.Test> {
			TestScreen()
		}
	}

}
