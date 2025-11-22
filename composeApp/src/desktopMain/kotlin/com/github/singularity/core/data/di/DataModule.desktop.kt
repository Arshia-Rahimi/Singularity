package com.github.singularity.core.data.di

import com.github.singularity.core.data.AuthTokenRepository
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.data.impl.BroadcastRepositoryImpl
import com.github.singularity.core.data.impl.HostedSyncGroupRepositoryImpl
import com.github.singularity.core.data.impl.RandomTokenAuthRepository
import com.github.singularity.core.data.impl.ServerConnectionRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformDataModule() {
    singleOf(::ServerConnectionRepositoryImpl) bind ServerConnectionRepository::class
    singleOf(::HostedSyncGroupRepositoryImpl) bind HostedSyncGroupRepository::class
    singleOf(::BroadcastRepositoryImpl) bind BroadcastRepository::class
    singleOf(::RandomTokenAuthRepository) bind AuthTokenRepository::class
}
