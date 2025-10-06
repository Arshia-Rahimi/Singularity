package com.github.singularity.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.currentRoute
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.compose.navigateAndClearStack
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.feature.broadcast.BroadcastScreen
import com.github.singularity.ui.feature.discover.DiscoverScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.home
import singularity.composeapp.generated.resources.log
import singularity.composeapp.generated.resources.logs
import singularity.composeapp.generated.resources.settings
import singularity.composeapp.generated.resources.singularity

@Composable
fun Navigation() {
    val viewModel = koinInject<NavigationViewModel>()
    val theme by viewModel.appTheme.collectAsStateWithLifecycle()
    val syncMode by viewModel.syncMode.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { scope.launch { drawerState.open() } }
    val closeDrawer = { scope.launch { drawerState.close() } }
    val homeRoute = if (syncMode == SyncMode.Client) Route.Discover else Route.Broadcast
    val currentRoute by navController.currentRoute

    LaunchedEffect(syncMode) {
        if (syncMode == SyncMode.Client) navController.navigateAndClearStack(Route.Discover)
        else navController.navigateAndClearStack(Route.Broadcast)
    }

    SingularityTheme(theme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = Res.string.singularity.getString(),
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary,
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
                            navController.navigate(homeRoute)
                            closeDrawer()
                        },
                        selected = homeRoute::class.simpleName == currentRoute,
                    )
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    NavigationDrawerItem(
                        label = { Text(Res.string.settings.getString()) },
                        icon = {
                            Icon(
                                painter = Res.drawable.settings.getPainter(),
                                contentDescription = Res.string.settings.getString(),
                            )
                        },
                        selected = Route.Settings::class.simpleName == currentRoute,
                        onClick = {
                            navController.navigate(Route.Settings)
                            closeDrawer()
                        },
                    )
                    NavigationDrawerItem(
                        label = { Text(Res.string.logs.getString()) },
                        icon = {
                            Icon(
                                painter = Res.drawable.log.getPainter(),
                                contentDescription = Res.string.logs.getString(),
                            )
                        },
                        selected = Route.Log::class.simpleName == currentRoute,
                        onClick = {
                            navController.navigate(Route.Log)
                            closeDrawer()
                        }
                    )
                }
            }
        ) {
            NavHost(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(500),
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(500),
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(500),
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(500),
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
    }
}
