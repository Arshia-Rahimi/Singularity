package com.github.singularity.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.app.navigation.Navigation
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
	KoinMultiplatformApplication(
		config = KoinConfig,
	) {
		val viewModel = koinViewModel<MainViewModel>()
		val uiState by viewModel.uiState.collectAsStateWithLifecycle()

		val density = LocalDensity.current
		CompositionLocalProvider(
			LocalDensity provides Density(
				density.density * uiState.scale,
				density.fontScale * uiState.scale,
			)
		) {
			SingularityTheme(uiState.theme) {
				Navigation()
			}
		}
	}
}
