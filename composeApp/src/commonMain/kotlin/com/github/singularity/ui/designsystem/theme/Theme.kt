package com.github.singularity.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.singularity.core.shared.AppTheme

private val lightColorScheme = lightColorScheme(
    primary = Color(0xFF8F4953),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFD9DC),
    onPrimaryContainer = Color(0xFF72333C),
    secondary = Color(0xFF765659),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFD9DC),
    onSecondaryContainer = Color(0xFF5C3F42),
    tertiary = Color(0xFF785830),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDDB7),
    onTertiaryContainer = Color(0xFF5E411B),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A),
    background = Color(0xFFFFF8F7),
    onBackground = Color(0xFF22191A),
    surface = Color(0xFFFFF8F7),
    onSurface = Color(0xFF22191A),
    surfaceVariant = Color(0xFFF4DDDE),
    onSurfaceVariant = Color(0xFF524344),
    outline = Color(0xFF847374),
    outlineVariant = Color(0xFFD7C1C3),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF382E2F),
    inverseOnSurface = Color(0xFFFFEDED),
    inversePrimary = Color(0xFFFFB2BA),
    surfaceDim = Color(0xFFE7D6D7),
    surfaceBright = Color(0xFFFFF8F7),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFF0F0),
    surfaceContainer = Color(0xFFFCEAEA),
    surfaceContainerHigh = Color(0xFFF6E4E5),
    surfaceContainerHighest = Color(0xFFF0DEDF),
)

private val darkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB2BA),
    onPrimary = Color(0xFF561D27),
    primaryContainer = Color(0xFF72333C),
    onPrimaryContainer = Color(0xFFFFD9DC),
    secondary = Color(0xFFE5BDC0),
    onSecondary = Color(0xFF43292C),
    secondaryContainer = Color(0xFF5C3F42),
    onSecondaryContainer = Color(0xFFFFD9DC),
    tertiary = Color(0xFFE9BF8F),
    onTertiary = Color(0xFF442B07),
    tertiaryContainer = Color(0xFF5E411B),
    onTertiaryContainer = Color(0xFFFFDDB7),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1A1112),
    onBackground = Color(0xFFF0DEDF),
    surface = Color(0xFF1A1112),
    onSurface = Color(0xFFF0DEDF),
    surfaceVariant = Color(0xFF524344),
    onSurfaceVariant = Color(0xFFD7C1C3),
    outline = Color(0xFF9F8C8D),
    outlineVariant = Color(0xFF524344),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFF0DEDF),
    inverseOnSurface = Color(0xFF382E2F),
    inversePrimary = Color(0xFF8F4953),
    surfaceDim = Color(0xFF1A1112),
    surfaceBright = Color(0xFF413737),
    surfaceContainerLowest = Color(0xFF140C0D),
    surfaceContainerLow = Color(0xFF22191A),
    surfaceContainer = Color(0xFF261D1E),
    surfaceContainerHigh = Color(0xFF312828),
    surfaceContainerHighest = Color(0xFF3D3233),
)

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
