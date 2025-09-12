package com.github.singularity.app.di

import com.github.singularity.core.broadcast.di.BroadcastModule
import com.github.singularity.core.client.di.ClientModule
import com.github.singularity.core.data.di.DataModule
import com.github.singularity.core.data.di.ServerDataModule
import com.github.singularity.core.database.di.DatabaseModule
import com.github.singularity.core.datastore.di.DataStoreModule
import com.github.singularity.core.server.di.ServerModule
import com.github.singularity.core.service.di.ClientServiceModule
import com.github.singularity.core.service.di.ServerServiceModule
import com.github.singularity.core.sync.di.PluginModule
import com.github.singularity.ui.di.ViewmodelModule

val ModulesList = listOf(
    ViewmodelModule,
    DataModule,
    DataStoreModule,
    DatabaseModule,
    ClientModule,
    PluginModule,
    BroadcastModule,
)

val ServerOnlyModules = listOf(
    ServerDataModule,
    ServerModule,
    ServerServiceModule,
)

val ClientOnlyModules = listOf(
    ClientServiceModule,
)
