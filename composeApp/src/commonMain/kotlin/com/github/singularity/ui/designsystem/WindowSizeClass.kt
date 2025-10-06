package com.github.singularity.ui.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo

enum class WindowSizeClass { Compact, Medium, Expanded }

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val localWindowInfo = LocalWindowInfo.current
    val screenWidth = localWindowInfo.containerSize.width

    return remember(screenWidth) {
        when {
            screenWidth < 600 -> WindowSizeClass.Compact
            screenWidth < 1600 -> WindowSizeClass.Medium
            else -> WindowSizeClass.Expanded
        }
    }
}
