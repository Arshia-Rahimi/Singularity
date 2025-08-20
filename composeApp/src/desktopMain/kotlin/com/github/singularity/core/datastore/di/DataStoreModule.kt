package com.github.singularity.core.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.github.singularity.core.shared.DataStoreFileName
import okio.Path.Companion.toPath
import org.koin.dsl.module
import java.io.File

actual val DataStoreModule = module {
    single {
        PreferenceDataStoreFactory.createWithPath(
            // todo change path
            produceFile = {
                File(
                    System.getProperty("java.io.tmpdir"),
                    DataStoreFileName
                ).absolutePath.toPath()
            },
            scope = get(),
        )
    }
}
