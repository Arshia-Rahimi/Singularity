package com.github.singularity.app.di

import com.github.singularity.core.broadcast.di.BroadcastModule
import com.github.singularity.core.client.di.ClientModule
import com.github.singularity.core.data.di.DataModule
import com.github.singularity.core.database.di.DatabaseModule
import com.github.singularity.core.server.di.ServerModule
import com.github.singularity.core.sync.di.SyncModule
import com.github.singularity.ui.di.ViewmodelModule

val ModulesList = listOf(
    ViewmodelModule,
    DataModule,
    DatabaseModule,
    ClientModule,
    SyncModule,
    BroadcastModule,
    ServerModule,
) 
