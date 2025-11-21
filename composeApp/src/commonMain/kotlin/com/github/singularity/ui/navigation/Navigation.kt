package com.github.singularity.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.github.singularity.app.MainUiState
import com.github.singularity.ui.feature.connection.ConnectionScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.permissions.PermissionsScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import com.github.singularity.ui.feature.test.TestScreen
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
	uiState: MainUiState,
	toggleSyncMode: () -> Unit,
) {
	NavigationDrawer(
		uiState = uiState,
		toggleSyncMode = toggleSyncMode,
	) {
		NavigationHost()
	}
}

@Composable
private fun NavigationHost() {
	val navViewModel = koinViewModel<NavigationViewmodel>()

	NavDisplay(
		backStack = navViewModel.backStack,
		onBack = AppNavigationController::navigateBack,
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator(),
		),
		entryProvider = entryProvider {
			entry<Route.Connection> {
				ConnectionScreen()
			}

			entry<Route.Permissions> {
				PermissionsScreen()
			}

			entry<Route.Settings> {
				SettingsScreen()
			}

			entry<Route.Log> {
				LogScreen()
			}

			entry<Route.Test> {
				TestScreen()
			}
		},
	)

}
