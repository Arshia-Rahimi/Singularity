package com.github.singularity.app.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.app.navigation.components.NavigationDrawerItem
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.compose.ObserveForEvents
import com.github.singularity.core.shared.compose.currentRoute
import com.github.singularity.core.shared.compose.rememberCanPopBackStack
import com.github.singularity.ui.designsystem.WindowSizeClass
import com.github.singularity.ui.designsystem.rememberWindowSizeClass
import com.github.singularity.ui.feature.home.HomeScreen
import com.github.singularity.ui.feature.log.LogScreen
import com.github.singularity.ui.feature.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    syncMode: SyncMode,
) {
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
                syncMode = syncMode,
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
                syncMode = syncMode,
            )
        }
    }

}

@Composable
private fun DrawerContent(
    navController: NavController,
    closeDrawer: () -> Unit,
) {
    val currentRoute by navController.currentRoute
    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(12.dp))

        DrawerTopBar()
        
        NavigationDrawerItem.entries.forEach { item ->
            NavigationDrawerItem(
                item = item,
                currentRoute = currentRoute,
                closeDrawer = closeDrawer,
                navigateTo = { navController.navigate(it) },
            )
        }
    }
}

@Composable
private fun NavigationHost(
    navController: NavHostController,
    syncMode: SyncMode,
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
            HomeScreen(
                syncMode = syncMode,
            )
        }
        composable<Route.Settings> {
            SettingsScreen(
            )
        }
        composable<Route.Log> {
            LogScreen()
        }
    }

    val canPopBackStack by navController.rememberCanPopBackStack()
    LaunchedEffect(canPopBackStack) {
        AppNavigationController.canPopBackStack(canPopBackStack)
    }

    ObserveForEvents(AppNavigationController.popStackEvent) {
        navController.popBackStack()
    }

}

@Composable
expect fun DrawerTopBar()
