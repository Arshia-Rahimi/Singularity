package com.github.singularity.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.github.singularity.core.shared.AppTheme

private val lightColorScheme = lightColorScheme()
private val darkColorScheme = darkColorScheme()

@Composable
fun SingularityTheme(
    theme: AppTheme,
    content: @Composable () -> Unit
) {
    val theme = when (theme) {
        AppTheme.Light -> lightColorScheme
        AppTheme.Dark -> darkColorScheme
        AppTheme.System -> if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme
    }
    
    MaterialTheme(
        colorScheme = theme,
        typography = Typography,
        content = content
    )
}
