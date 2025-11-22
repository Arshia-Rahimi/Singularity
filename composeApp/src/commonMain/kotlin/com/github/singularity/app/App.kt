package com.github.singularity.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.data.di.DataModule
import com.github.singularity.core.datasource.di.DataSourceModule
import com.github.singularity.core.log.di.LoggerModule
import com.github.singularity.core.syncservice.di.SyncServiceModule
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.di.ViewmodelModule
import com.github.singularity.ui.navigation.Navigation
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

private val AppModule = module {
    viewModelOf(::MainViewModel)
}

private val KoinConfig = KoinConfiguration {
    modules(
        AppModule,
        ViewmodelModule,
        DataModule,
        SyncServiceModule,
        LoggerModule,
        DataSourceModule,
    )
}

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
                Navigation(
                    uiState = uiState,
                    toggleSyncMode = viewModel::toggleSyncMode,
                )
            }
        }
    }
}
