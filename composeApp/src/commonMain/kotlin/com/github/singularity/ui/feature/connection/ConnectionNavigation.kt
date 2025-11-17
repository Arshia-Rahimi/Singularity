package com.github.singularity.ui.feature.connection

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.singularity.ui.navigation.Route

fun NavGraphBuilder.connectionNavigation() {
	composable<Route.Connection> {
		ConnectionScreen()
	}
}
