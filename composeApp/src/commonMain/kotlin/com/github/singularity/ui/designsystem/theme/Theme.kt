package com.github.singularity.ui.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.ThemeOption
import com.materialkolor.rememberDynamicColorScheme

@Composable
fun SingularityTheme(
    theme: AppTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = rememberDynamicColorScheme(
        primary = theme.customPrimaryColor,
        isDark = theme.themeOption in listOf(ThemeOption.Dark, ThemeOption.Amoled),
        isAmoled = theme.themeOption == ThemeOption.Amoled,
    )
	MaterialTheme(
        colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}
