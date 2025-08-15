package com.github.singularity.android.app

import android.app.Application
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
class SingularityApplication: Application(), KoinStartup {

    override fun onKoinStartup(): KoinConfiguration {
        TODO("Not yet implemented")
    }
}
