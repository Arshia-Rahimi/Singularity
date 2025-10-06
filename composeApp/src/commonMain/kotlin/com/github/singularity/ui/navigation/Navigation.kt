package com.github.singularity.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
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
import androidx.compose.runtime.LaunchedEffect
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
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.currentRoute
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.compose.navigateAndClearStack
import com.github.singularity.ui.designsystem.WindowSizeClass
import com.github.singularity.ui.designsystem.rememberWindowSizeClass
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.feature.broadcast.BroadcastScreen
import com.github.singularity.ui.feature.discover.DiscoverScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import com.github.singularity.ui.navigation.components.NavigationDrawerItem
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.home
import singularity.composeapp.generated.resources.singularity

@Composable
fun Navigation() {
    val viewModel = koinInject<NavigationViewModel>()
    val theme by viewModel.appTheme.collectAsStateWithLifecycle()
    val syncMode by viewModel.syncMode.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val windowSizeClass = rememberWindowSizeClass()

    val homeRoute = if (syncMode == SyncMode.Client) Route.Discover else Route.Broadcast
    val currentRoute by navController.currentRoute

    LaunchedEffect(syncMode) {
        if (syncMode == SyncMode.Client) navController.navigateAndClearStack(Route.Discover)
        else navController.navigateAndClearStack(Route.Broadcast)
    }

    SingularityTheme(theme) {

        if (windowSizeClass == WindowSizeClass.Expanded) {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet(
                        modifier = Modifier.width(IntrinsicSize.Max),
                    ) {
                        DrawerContent(
                            navController = navController,
                            homeRoute = homeRoute,
                            currentRoute = currentRoute,
                            closeDrawer = {},
                        )
                    }
                },
            ) {
                NavigationHost(
                    navController = navController,
                    openDrawer = {},
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
                            homeRoute = homeRoute,
                            currentRoute = currentRoute,
                            closeDrawer = closeDrawer,
                        )
                    }
                }
            ) {
                NavigationHost(
                    navController = navController,
                    openDrawer = toggleDrawer,
                )
            }
        }
    }

}

@Composable
private fun DrawerContent(
    navController: NavController,
    homeRoute: Route,
    currentRoute: String?,
    closeDrawer: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            text = Res.string.singularity.getString(),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        NavigationDrawerItem(
            label = { Text(Res.string.home.getString()) },
            icon = {
                Icon(
                    painter = Res.drawable.home.getPainter(),
                    contentDescription = Res.string.home.getString(),
                )
            },
            onClick = {
                if (currentRoute != homeRoute::class.simpleName) {
                    navController.navigate(homeRoute)
                }
                closeDrawer()
            },
            selected = homeRoute::class.simpleName == currentRoute,
        )
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
    openDrawer: () -> Unit,
) {
    NavHost(
        enterTransition = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(250),
            )
        },
        popEnterTransition = {
            slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(250),
            )
        },
        exitTransition = {
            slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(250),
            )
        },
        popExitTransition = {
            slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(250),
            )
        },
        navController = navController,
        startDestination = Route.Discover,
    ) {
        composable<Route.Discover> {
            DiscoverScreen(
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
        if (canHostSyncServer) {
            composable<Route.Broadcast> {
                BroadcastScreen(
                    openDrawer = openDrawer,
                )
            }
        }
    }
}
