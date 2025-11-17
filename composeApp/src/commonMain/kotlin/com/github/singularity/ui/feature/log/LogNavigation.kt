package com.github.singularity.ui.feature.log

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.singularity.ui.navigation.Route

fun NavGraphBuilder.logNavigation() {
	composable<Route.Log> {
		LogScreen()
	}
}
