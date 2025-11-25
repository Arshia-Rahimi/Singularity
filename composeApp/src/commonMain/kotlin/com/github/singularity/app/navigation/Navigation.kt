package com.github.singularity.app.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.github.singularity.app.navigation.components.NavigationDrawer
import com.github.singularity.ui.feature.connection.ConnectionScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.plugins.PluginsScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import com.github.singularity.ui.feature.test.TestScreen
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
	NavigationDrawer {
        NavigationHost()
    }
}

@Composable
private fun NavigationHost() {
	val navigator = koinInject<Navigator>()

    NavDisplay(
	    backStack = navigator.backStack,
	    onBack = navigator::navigateUp,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        transitionSpec = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
            ) togetherWith fadeOut()
        },
        popTransitionSpec = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
            ) togetherWith fadeOut()
        },
    ) { route ->
        when (route) {
            is Route.Connection -> NavEntry(route) {
                ConnectionScreen()
            }

	        is Route.Plugins -> NavEntry(route) {
		        PluginsScreen()
	        }

            is Route.Settings -> NavEntry(route) {
                SettingsScreen()
            }

            is Route.Log -> NavEntry(route) {
                LogScreen()
            }

            is Route.Test -> NavEntry(route) {
                TestScreen()
            }
        }
    }

}
