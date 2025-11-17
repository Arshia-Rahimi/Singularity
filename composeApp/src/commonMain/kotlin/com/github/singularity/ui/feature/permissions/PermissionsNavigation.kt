package com.github.singularity.ui.feature.permissions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.singularity.ui.navigation.Route

fun NavGraphBuilder.permissionsNavigation() {
	composable<Route.Permissions> {
		PermissionsScreen()
	}
}
