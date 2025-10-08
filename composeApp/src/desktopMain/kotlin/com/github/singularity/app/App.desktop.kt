package com.github.singularity.app

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.close
import singularity.composeapp.generated.resources.expand
import singularity.composeapp.generated.resources.fullscreen
import singularity.composeapp.generated.resources.minimize
import singularity.composeapp.generated.resources.window

@Composable
actual fun AppWindow(content: @Composable (() -> Unit)) {
    val windowController = LocalWindowController.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(40.dp)
                .background(MaterialTheme.colorScheme.surface)
                .combinedClickable(
                    onClick = {},
                    onDoubleClick = windowController::toggleMaximize,
                    interactionSource = null,
                    indication = null,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            WindowTitle()
            WindowIcons()
        }

        content()
    }
}

@Composable
private fun WindowIcons() {
    val windowController = LocalWindowController.current

    Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = windowController::minimize,
        ) {
            Icon(
                painter = Res.drawable.minimize.getPainter(),
                contentDescription = Res.string.minimize.getString(),
            )
        }

        if (windowController.isMaximized) {
            TextButton(
                onClick = windowController::restore,
            ) {
                Icon(
                    painter = Res.drawable.window.getPainter(),
                    contentDescription = Res.string.window.getString(),
                )
            }
        } else {
            TextButton(
                onClick = windowController::maximize,
            ) {
                Icon(
                    painter = Res.drawable.expand.getPainter(),
                    contentDescription = Res.string.fullscreen.getString(),
                )
            }
        }

        TextButton(
            onClick = windowController::close,
        ) {
            Icon(
                painter = Res.drawable.close.getPainter(),
                contentDescription = Res.string.close.getString(),
            )
        }
    }
}

@Composable
private fun RowScope.WindowTitle() {
    Row(
        modifier = Modifier.fillMaxHeight()
            .weight(1f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("singularity")
    }
}
