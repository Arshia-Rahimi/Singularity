package com.github.singularity.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.app.navigation.Navigation
import com.github.singularity.core.broadcast.di.BroadcastModule
import com.github.singularity.core.client.di.ClientModule
import com.github.singularity.core.data.di.DataModule
import com.github.singularity.core.database.di.DatabaseModule
import com.github.singularity.core.log.di.LoggerModule
import com.github.singularity.core.server.di.ServerModule
import com.github.singularity.core.sync.di.SyncModule
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.di.ViewmodelModule
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

private val AppModule = module {
    viewModelOf(::MainViewModel)
}

private val config = KoinConfiguration {
    modules(
        AppModule,
        ViewmodelModule,
        DataModule,
        DatabaseModule,
        ClientModule,
        SyncModule,
        BroadcastModule,
        ServerModule,
        LoggerModule,
    )
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    KoinMultiplatformApplication(
        config = config,
    ) {
        val viewModel = koinInject<MainViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        SingularityTheme(uiState.theme) {
            AppWindow {
                Navigation(
                    syncMode = uiState.syncMode,
                )
            }
        }
    }
}

@Composable
expect fun AppWindow(content: @Composable () -> Unit)
