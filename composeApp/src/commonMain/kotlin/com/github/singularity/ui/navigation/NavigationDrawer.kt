package com.github.singularity.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.github.singularity.ui.designsystem.shared.ObserveForEvents
import com.github.singularity.ui.designsystem.shared.PainterIconButton
import com.github.singularity.ui.designsystem.shared.WindowSizeClass
import com.github.singularity.ui.designsystem.shared.currentRoute
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.designsystem.shared.onCondition
import com.github.singularity.ui.designsystem.shared.rememberWindowSizeClass
import kotlinx.coroutines.launch
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.singularity

@Composable
fun NavigationDrawer(
	navController: NavHostController,
	navHost: @Composable () -> Unit,
) {
	val windowSizeClass by rememberWindowSizeClass()

	if (windowSizeClass == WindowSizeClass.Expanded) {
		PermanentNavigationDrawer(
			drawerContent = {
				PermanentDrawerSheet(
					modifier = Modifier.width(IntrinsicSize.Max),
				) {
					DrawerContent(
						navController = navController,
						closeDrawer = {},
					)
				}
			},
		) {
			navHost()
		}

	} else {

		val scope = rememberCoroutineScope()
		val drawerState = rememberDrawerState(DrawerValue.Closed)
		val closeDrawer: () -> Unit = { scope.launch { drawerState.close() } }

		ObserveForEvents(AppNavigationController.toggleDrawerEvent) {
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
					DrawerContent(
						navController = navController,
						closeDrawer = closeDrawer,
					)
				}
			}
		) {
			navHost()
		}
	}

}

@Composable
private fun DrawerContent(navController: NavController, closeDrawer: () -> Unit) {
	val windowSizeClass by rememberWindowSizeClass()
	val currentRoute by navController.currentRoute

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(vertical = 12.dp)
			.padding(horizontal = 8.dp)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.SpaceBetween,
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
			modifier = Modifier.fillMaxWidth(),
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
			) {
				if (windowSizeClass == WindowSizeClass.Expanded) {
					val enabled by AppNavigationController.canPopBackStack.collectAsStateWithLifecycle()
					PainterIconButton(
						onClick = AppNavigationController::popBackStack,
						image = Res.drawable.arrow_back,
						contentDescription = Res.string.back,
						enabled = enabled,
					)
				}
				Text(
					text = Res.string.singularity.getString(),
					fontSize = 24.sp,
					color = MaterialTheme.colorScheme.primary,
					modifier = Modifier.onCondition(windowSizeClass != WindowSizeClass.Expanded) {
						padding(start = 12.dp)
					}
				)
			}

			HorizontalDivider(Modifier.padding(vertical = 4.dp))

			NavigationDrawerItemTop.entries.forEach { item ->
				NavigationDrawerItem(
					item = item,
					currentRoute = currentRoute,
					closeDrawer = closeDrawer,
					navigateTo = { navController.navigate(it) },
				)
			}

		}

		Column(
			modifier = Modifier.fillMaxWidth(),
			verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
		) {
			NavigationDrawerItemBottom.entries.forEach { item ->
				NavigationDrawerItem(
					item = item,
					currentRoute = currentRoute,
					closeDrawer = closeDrawer,
					navigateTo = { navController.navigate(it) },
				)
			}
		}
	}
}
