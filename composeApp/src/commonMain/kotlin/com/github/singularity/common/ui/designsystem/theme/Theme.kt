package com.github.singularity.common.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val lightColorScheme = lightColorScheme()
private val darkColorScheme = darkColorScheme()

@Composable
fun SingularityTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme,
        typography = Typography,
        content = content
    )
}
