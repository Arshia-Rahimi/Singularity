package com.github.singularity.app.di

import com.github.singularity.app.navigation.NavigationViewModel
import com.github.singularity.core.client.di.ClientModule
import com.github.singularity.core.data.di.DataModule
import com.github.singularity.core.data.di.ServerDataModule
import com.github.singularity.core.database.di.DatabaseModule
import com.github.singularity.core.datastore.di.DataStoreModule
import com.github.singularity.core.mdns.canHostSyncServer
import com.github.singularity.core.mdns.di.MdnsModule
import com.github.singularity.core.server.di.ServerModule
import com.github.singularity.ui.di.ViewmodelModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val MainModule = module {

    factory { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

    viewModelOf(::NavigationViewModel)

}

val ModulesList: List<Module> = buildList {
    add(MainModule)
    add(ViewmodelModule)
    add(DataModule)
    add(DataStoreModule)
    add(MdnsModule)
    add(DatabaseModule)
    add(ClientModule)

    // dependencies that are only for clients that can host local server
    if (canHostSyncServer) {
        add(ServerDataModule)
        add(ServerModule)
    }
}
