package com.github.singularity.core.data.di

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.data.impl.BroadcastRepositoryImp
import com.github.singularity.core.data.impl.HostedSyncGroupRepositoryImpl
import com.github.singularity.core.data.impl.LocalServerAuthRepository
import com.github.singularity.core.data.impl.ServerConnectionRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServerDataModule = module {
    singleOf(::ServerConnectionRepositoryImpl) bind ServerConnectionRepository::class
    singleOf(::HostedSyncGroupRepositoryImpl) bind HostedSyncGroupRepository::class
    singleOf(::BroadcastRepositoryImp) bind BroadcastRepository::class
    singleOf(::LocalServerAuthRepository) bind AuthRepository::class
}
