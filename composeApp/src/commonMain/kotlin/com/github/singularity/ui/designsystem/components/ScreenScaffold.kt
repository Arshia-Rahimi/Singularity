package com.github.singularity.ui.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.singularity.ui.designsystem.PainterIconButton
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.drawer
import singularity.composeapp.generated.resources.navigation_drawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    topBar: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = topBar,
        floatingActionButton = floatingActionButton,
    ) { ip ->
        content(ip)
    }
}

@Composable
fun DrawerIcon(
    openDrawer: () -> Unit
) {
    PainterIconButton(
        onClick = openDrawer,
        image = Res.drawable.drawer,
        contentDescription = Res.string.navigation_drawer,
    )
}
