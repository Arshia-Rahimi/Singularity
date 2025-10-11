package com.github.singularity.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
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
import org.koin.compose.koinInject
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val AppModule = module {
    viewModelOf(::MainViewModel)
}

val CommonModules = listOf(
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

private var koinApp: KoinApplication? = null

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    if (koinApp != null) return
    koinApp = startKoin {
        appDeclaration()
        modules(CommonModules)
    }
}

@Composable
fun App() {
    val viewModel = koinInject<MainViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState?.let { uiState ->
        val density = LocalDensity.current
        CompositionLocalProvider(
            LocalDensity provides Density(
                density.density * uiState.scale.value,
                density.fontScale * uiState.scale.value
            )
        ) {
            SingularityTheme(uiState.theme) {
                Navigation(
                    syncMode = uiState.syncMode,
                )
            }
        }
    }
}
