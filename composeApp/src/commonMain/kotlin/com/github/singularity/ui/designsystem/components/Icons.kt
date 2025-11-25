package com.github.singularity.ui.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.github.singularity.app.navigation.Navigator
import com.github.singularity.ui.designsystem.shared.PainterIconButton
import com.github.singularity.ui.designsystem.shared.WindowSizeClass
import com.github.singularity.ui.designsystem.shared.rememberWindowSizeClass
import org.koin.compose.koinInject
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.drawer
import singularity.composeapp.generated.resources.navigation_drawer

@Composable
fun DrawerIcon() {
    val windowSizeClass by rememberWindowSizeClass()
	val navigator = koinInject<Navigator>()
    if (windowSizeClass != WindowSizeClass.Expanded) {
        PainterIconButton(
	        onClick = navigator::toggleDrawer,
            image = Res.drawable.drawer,
            contentDescription = Res.string.navigation_drawer,
        )
    }
}
