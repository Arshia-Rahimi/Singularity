package com.github.singularity.ui.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.ui.designsystem.PainterIconButton
import com.github.singularity.ui.designsystem.WindowSizeClass
import com.github.singularity.ui.designsystem.rememberWindowSizeClass
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.drawer
import singularity.composeapp.generated.resources.navigation_drawer

@Composable
fun DrawerIcon() {
    val windowSizeClass by rememberWindowSizeClass()
    if (windowSizeClass != WindowSizeClass.Expanded) {
        PainterIconButton(
            onClick = AppNavigationController::toggleDrawer,
            image = Res.drawable.drawer,
            contentDescription = Res.string.navigation_drawer,
        )
    }
}
