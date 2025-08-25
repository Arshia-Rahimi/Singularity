package com.github.singularity.app.di

import com.github.singularity.app.navigation.NavigationViewModel
import com.github.singularity.core.client.di.ClientModule
import com.github.singularity.core.data.di.DataModule
import com.github.singularity.core.database.di.DatabaseModule
import com.github.singularity.core.datastore.di.DataStoreModule
import com.github.singularity.core.mdns.di.MdnsModule
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

val ModulesList: List<Module> = listOf(
    MainModule,
    ViewmodelModule,
    DataModule,
    DataStoreModule,
    MdnsModule,
    DatabaseModule,
    ClientModule,
)
