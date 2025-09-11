package com.github.singularity.ui.designsystem.theme

import androidx.annotation.FloatRange
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.singularity.core.shared.AppTheme

private val lightColorScheme = lightColorScheme(
    primary = Color.DarkGray.blend(Color.Black, by = 0.2f),
    secondary = Color.DarkGray.blend(Color.Black, by = 0.2f),
    tertiary = Color.DarkGray.blend(Color.Black, by = 0.1f),
    surface = Color.White.blend(Color.LightGray, by = 0.1f),
    secondaryContainer = Color.White.blend(Color.LightGray, by = 0.7f),
    onSecondaryContainer = Color.Black.blend(Color.LightGray, by = 0.3f),
    onPrimaryContainer = Color.Black.blend(Color.LightGray, by = 0.3f),
    onSurfaceVariant = Color.Black.blend(Color.LightGray, by = 0.3f),
    onPrimary = Color.White.blend(Color.LightGray, by = 0.2f),
    surfaceContainer = Color.White.blend(Color.LightGray, by = 0.4f),
)

private val darkColorScheme = darkColorScheme(
    primary = Color.LightGray,
    secondary = Color.LightGray,
    tertiary = Color.LightGray,
    surface = Color.DarkGray.blend(Color.Black, by = 0.7f),
    secondaryContainer = Color.DarkGray.blend(Color.Black, by = 0.3f),
    onPrimaryContainer = Color.White.blend(Color.LightGray, by = 0.4f),
    onSecondaryContainer = Color.White.blend(Color.LightGray, by = 0.3f),
    onSurfaceVariant = Color.White.blend(Color.LightGray, by = 0.25f),
    onPrimary = Color.Black.blend(Color.LightGray, by = 0.2f),
    surfaceContainer = Color.Black.blend(Color.LightGray, by = 0.3f),
)

fun Color.blend(to: Color, @FloatRange(0.0, 1.0) by: Float): Color {
    require(by in 0f..1f) { "by must be between 0 and 1" }

    return Color(
        red = red + (to.red - red) * by,
        green = green + (to.green - green) * by,
        blue = blue + (to.blue - blue) * by,
        alpha = alpha + (to.alpha - alpha) * by
    )
}


@Composable
fun SingularityTheme(
    theme: AppTheme,
    content: @Composable () -> Unit
) {
    val theme = when (theme) {
        AppTheme.Dark -> darkColorScheme
        AppTheme.System if isSystemInDarkTheme() -> darkColorScheme
        else -> lightColorScheme
    }
    
    MaterialTheme(
        colorScheme = theme,
        typography = Typography,
        content = content
    )
}
