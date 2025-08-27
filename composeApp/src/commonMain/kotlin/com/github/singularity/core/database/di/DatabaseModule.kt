package com.github.singularity.core.database.di

import com.github.singularity.core.database.LocalHostedSyncGroupsDataSource
import com.github.singularity.core.database.LocalJoinedSyncGroupsDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect fun Module.driver()

val DatabaseModule = module {
    singleOf(::LocalHostedSyncGroupsDataSource)
    singleOf(::LocalJoinedSyncGroupsDataSource)
    driver()
}
