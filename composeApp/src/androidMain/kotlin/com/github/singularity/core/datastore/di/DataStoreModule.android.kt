package com.github.singularity.core.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.github.singularity.core.shared.DataStoreFileName
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val DataStoreModule = module {
    single {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { androidContext().filesDir.resolve(DataStoreFileName).absolutePath.toPath() },
            scope = get(),
        )
    }
}
