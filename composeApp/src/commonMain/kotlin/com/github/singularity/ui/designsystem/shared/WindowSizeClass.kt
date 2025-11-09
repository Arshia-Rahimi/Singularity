package com.github.singularity.ui.designsystem.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo

enum class WindowSizeClass { Compact, Medium, Expanded }

@Composable
fun rememberWindowSizeClass(): State<WindowSizeClass> {
    val localWindowInfo = LocalWindowInfo.current
    val screenWidth = localWindowInfo.containerSize.width

    return remember(screenWidth) {
        derivedStateOf {
            when {
                screenWidth < 600 -> WindowSizeClass.Compact
                screenWidth < 1600 -> WindowSizeClass.Medium
                else -> WindowSizeClass.Expanded
            }
        }
    }
}
