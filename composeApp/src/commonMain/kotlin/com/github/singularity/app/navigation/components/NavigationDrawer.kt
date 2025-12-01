package com.github.singularity.app.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.app.navigation.Navigator
import com.github.singularity.ui.designsystem.shared.ObserveForEvents
import com.github.singularity.ui.designsystem.shared.WindowSizeClass
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.designsystem.shared.onCondition
import com.github.singularity.ui.designsystem.shared.rememberWindowSizeClass
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.singularity

@Composable
fun NavigationDrawer(
	navHost: @Composable () -> Unit,
) {
	val windowSizeClass by rememberWindowSizeClass()

	if (windowSizeClass == WindowSizeClass.Expanded) {
		PermanentNavigationDrawer(
			drawerContent = {
				PermanentDrawerSheet(
					modifier = Modifier.width(IntrinsicSize.Max),
				) {
					DrawerContent()
				}
			},
		) {
			navHost()
		}

	} else {

		val navigator = koinInject<Navigator>()
		val scope = rememberCoroutineScope()
		val drawerState = rememberDrawerState(DrawerValue.Closed)
		val closeDrawer: () -> Unit = { scope.launch { drawerState.close() } }

		ObserveForEvents(navigator.toggleDrawerEvent) {
			scope.launch {
				if (drawerState.isOpen) drawerState.close()
				else drawerState.open()
			}
		}

		ModalNavigationDrawer(
			drawerState = drawerState,
			drawerContent = {
				ModalDrawerSheet(
					modifier = Modifier.width(IntrinsicSize.Max),
				) {
					DrawerContent(closeDrawer)
				}
			}
		) {
			navHost()
		}
	}

}

@Composable
private fun DrawerContent(
	closeDrawer: () -> Unit = {},
) {
	val navigator = koinInject<Navigator>()
	val backStack = navigator.backStack
	val currentRoute by remember(backStack) { derivedStateOf { backStack.last() } }
	val windowSizeClass by rememberWindowSizeClass()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(vertical = 12.dp)
			.padding(horizontal = 8.dp)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.SpaceBetween,
	) {
		Column(
			modifier = Modifier.fillMaxWidth(),
		) {
			Text(
				text = Res.string.singularity.getString(),
				fontSize = 24.sp,
				color = MaterialTheme.colorScheme.primary,
				modifier = Modifier.onCondition(windowSizeClass != WindowSizeClass.Expanded) {
					padding(start = 12.dp)
				}
			)

			HorizontalDivider(Modifier.padding(vertical = 4.dp))

			SyncModeSwitch()

			NavigationDrawerItemTop.entries.forEach { item ->
				NavigationDrawerItem(
					item = item,
					currentRoute = currentRoute,
					closeDrawer = closeDrawer,
					navigateTo = navigator::navigateTo,
				)
			}

		}

		Column(
			modifier = Modifier.fillMaxWidth(),
		) {
			NavigationDrawerItemBottom.entries.forEach { item ->
				NavigationDrawerItem(
					item = item,
					currentRoute = currentRoute,
					closeDrawer = closeDrawer,
					navigateTo = navigator::navigateTo,
				)
			}
		}
	}
}
