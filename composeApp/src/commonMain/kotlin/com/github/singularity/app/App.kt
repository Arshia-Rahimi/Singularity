package com.github.singularity.app

import androidx.compose.runtime.Composable
import com.github.singularity.core.broadcast.di.BroadcastModule
import com.github.singularity.core.client.di.ClientModule
import com.github.singularity.core.data.di.DataModule
import com.github.singularity.core.database.di.DatabaseModule
import com.github.singularity.core.server.di.ServerModule
import com.github.singularity.core.sync.di.SyncModule
import com.github.singularity.ui.di.ViewmodelModule
import com.github.singularity.ui.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

private val config = KoinConfiguration {
    modules(
        ViewmodelModule,
        DataModule,
        DatabaseModule,
        ClientModule,
        SyncModule,
        BroadcastModule,
        ServerModule,
        module {
            single { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
        },
    )
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    KoinMultiplatformApplication(
        config = config,
    ) {
        Navigation()
    }
} 
