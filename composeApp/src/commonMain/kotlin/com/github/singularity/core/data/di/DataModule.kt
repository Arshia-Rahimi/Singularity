package com.github.singularity.core.data.di

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.impl.BroadcastRepositoryImp
import com.github.singularity.core.data.impl.DiscoverRepositoryImp
import com.github.singularity.core.data.impl.OfflinePreferencesRepository
import com.github.singularity.core.mdns.canHostSyncServer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DataModule = module {
    factoryOf(::OfflinePreferencesRepository) bind PreferencesRepository::class
    factoryOf(::DiscoverRepositoryImp) bind DiscoverRepository::class

    // disable broadcast feature for targets that can't host the server
    if (canHostSyncServer) factoryOf(::BroadcastRepositoryImp) bind BroadcastRepository::class
}
