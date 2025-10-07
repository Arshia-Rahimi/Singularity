package com.github.singularity.ui.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.compose.currentRoute
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.compose.isInGraphRoot
import com.github.singularity.ui.designsystem.PainterIconButton
import com.github.singularity.ui.designsystem.WindowSizeClass
import com.github.singularity.ui.designsystem.rememberWindowSizeClass
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.feature.home.HomeScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import com.github.singularity.ui.navigation.components.NavigationDrawerItem
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.singularity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val viewModel = koinInject<NavigationViewModel>()
    val theme by viewModel.appTheme.collectAsStateWithLifecycle()
    val syncMode by viewModel.syncMode.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val windowSizeClass = rememberWindowSizeClass()

    val currentRoute by navController.currentRoute

    SingularityTheme(theme ?: AppTheme.System) {
        if (windowSizeClass == WindowSizeClass.Expanded) {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet(
                        modifier = Modifier.width(IntrinsicSize.Max),
                    ) {
                        DrawerContent(
                            navController = navController,
                            currentRoute = currentRoute,
                            closeDrawer = {},
                            windowSizeClass = windowSizeClass,
                        )
                    }
                },
            ) {
                NavigationHost(
                    navController = navController,
                    openDrawer = {},
                    syncMode = syncMode,
                )
            }

        } else {
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val closeDrawer: () -> Unit = { scope.launch { drawerState.close() } }
            val toggleDrawer: () -> Unit = {
                scope.launch {
                    if (drawerState.isClosed) drawerState.open()
                    else drawerState.close()
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
                            currentRoute = currentRoute,
                            closeDrawer = closeDrawer,
                            windowSizeClass = windowSizeClass,
                        )
                    }
                }
            ) {
                NavigationHost(
                    navController = navController,
                    syncMode = syncMode,
                    openDrawer = toggleDrawer,
                )
            }
        }
    }

}

@Composable
private fun DrawerContent(
    navController: NavController,
    currentRoute: String?,
    windowSizeClass: WindowSizeClass,
    closeDrawer: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (windowSizeClass == WindowSizeClass.Expanded) {
                PainterIconButton(
                    onClick = navController::popBackStack,
                    image = Res.drawable.arrow_back,
                    contentDescription = Res.string.back,
                    enabled = navController.isInGraphRoot,
                )
            }
            Text(
                text = Res.string.singularity.getString(),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        NavigationDrawerItem.entries.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label.getString()) },
                icon = {
                    Icon(
                        painter = item.icon.getPainter(),
                        contentDescription = item.label.getString(),
                    )
                },
                onClick = {
                    if (currentRoute != item.route::class.simpleName) {
                        navController.navigate(item.route)
                    }
                    closeDrawer()
                },
                selected = item.route::class.simpleName == currentRoute,
            )

            if (item.isFollowedByDivider) {
                HorizontalDivider(Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
private fun NavigationHost(
    navController: NavHostController,
    syncMode: SyncMode,
    openDrawer: () -> Unit,
) {
    NavHost(
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
        navController = navController,
        startDestination = Route.Home,
    ) {
        composable<Route.Home> {
            HomeScreen(
                syncMode = syncMode,
                openDrawer = openDrawer,
            )
        }
        composable<Route.Settings> {
            SettingsScreen(
                openDrawer = openDrawer,
            )
        }
        composable<Route.Log> {
            LogScreen(
                openDrawer = openDrawer,
            )
        }
    }
}
