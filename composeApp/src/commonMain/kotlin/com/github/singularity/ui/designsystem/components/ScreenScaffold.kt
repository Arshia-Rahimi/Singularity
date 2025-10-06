package com.github.singularity.ui.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.drawer
import singularity.composeapp.generated.resources.navigation_drawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    openDrawer: () -> Unit,
    topBarTitle: @Composable () -> Unit,
    topBarActions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = topBarTitle,
                navigationIcon = {
                    DrawerIcon(openDrawer)
                },
                actions = topBarActions,
            )
        },
        floatingActionButton = floatingActionButton,
    ) { ip ->
        content(ip)
    }
}

@Composable
private fun DrawerIcon(
    openDrawer: () -> Unit
) {
    IconButton(
        onClick = openDrawer,
    ) {
        Icon(
            painter = Res.drawable.drawer.getPainter(),
            contentDescription = Res.string.navigation_drawer.getString(),
        )
    }
}
