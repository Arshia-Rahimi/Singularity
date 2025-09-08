package com.github.singularity.app

import android.app.Application
import com.github.singularity.app.di.ClientOnlyModules
import com.github.singularity.app.di.ModulesList
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
class SingularityApplication: Application(), KoinStartup {

    override fun onKoinStartup() = KoinConfiguration {
        androidContext(this@SingularityApplication)
        modules(ModulesList + ClientOnlyModules)
    }

}
