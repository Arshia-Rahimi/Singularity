package com.github.singularity.app

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.AppTheme

@Immutable
data class MainUiState(
	val theme: AppTheme = AppTheme(),
	val scale: Float = 1f,
)
