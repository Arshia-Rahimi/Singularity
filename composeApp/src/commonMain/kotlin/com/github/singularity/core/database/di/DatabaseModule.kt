package com.github.singularity.core.database.di

import com.github.singularity.core.database.JoinedSyncGroupDataSource
import com.github.singularity.core.database.impl.LocalHostedSyncGroupsDataSource
import com.github.singularity.core.database.impl.LocalJoinedSyncGroupsDataSource
import com.github.singularity.data.HostedSyncGroupDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.driver()

val DatabaseModule = module {
    driver()
    singleOf(::LocalHostedSyncGroupsDataSource) bind HostedSyncGroupDataSource::class
    singleOf(::LocalJoinedSyncGroupsDataSource) bind JoinedSyncGroupDataSource::class
}
