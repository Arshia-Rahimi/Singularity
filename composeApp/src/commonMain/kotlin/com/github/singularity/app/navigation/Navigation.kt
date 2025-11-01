package com.github.singularity.app.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
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
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.app.navigation.components.NavigationDrawerItem
import com.github.singularity.app.navigation.components.NavigationDrawerItemBottom
import com.github.singularity.app.navigation.components.NavigationDrawerItemTop
import com.github.singularity.core.shared.compose.ObserveForEvents
import com.github.singularity.core.shared.compose.currentRoute
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.compose.onCondition
import com.github.singularity.core.shared.compose.rememberCanPopBackStack
import com.github.singularity.ui.designsystem.PainterIconButton
import com.github.singularity.ui.designsystem.WindowSizeClass
import com.github.singularity.ui.designsystem.rememberWindowSizeClass
import com.github.singularity.ui.feature.connection.ConnectionScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import com.github.singularity.ui.feature.sync.SyncScreen
import com.github.singularity.ui.feature.test.TestScreen
import kotlinx.coroutines.launch
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.singularity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
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
            NavigationHost(
                navController = navController,
            )
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
            NavigationHost(
                navController = navController,
            )
        }
    }

}

@Composable
private fun DrawerContent(
    navController: NavController,
    closeDrawer: () -> Unit,
) {
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

@Composable
private fun NavigationHost(
    navController: NavHostController,
) {
    NavHost(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
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
        exitTransition = {
            fadeOut()
        },
        navController = navController,
        startDestination = Route.Home,
    ) {
        composable<Route.Home> {
            ConnectionScreen()
        }
        composable<Route.Settings> {
            SettingsScreen()
        }
        composable<Route.Log> {
            LogScreen()
        }
        composable<Route.Sync> {
            SyncScreen()
        }
        composable<Route.Test> {
            TestScreen()
        }
    }

    val canPopBackStack by navController.rememberCanPopBackStack()
    LaunchedEffect(canPopBackStack) {
        AppNavigationController.setCanPopBackStack(canPopBackStack)
    }

    ObserveForEvents(AppNavigationController.popStackEvent) {
        navController.popBackStack()
    }

}
