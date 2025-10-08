package com.github.singularity.app.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.PainterIconButton
import com.github.singularity.ui.designsystem.WindowSizeClass
import com.github.singularity.ui.designsystem.rememberWindowSizeClass
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.singularity

@Composable
actual fun DrawerTopBar() {
    val windowSizeClass by rememberWindowSizeClass()

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
        )
    }

    HorizontalDivider(Modifier.padding(vertical = 8.dp))

}
