package com.github.singularity.ui.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.singularity.ui.navigation.Route

fun NavGraphBuilder.settingsNavigation() {
	composable<Route.Settings> {
		SettingsScreen()
	}
}
