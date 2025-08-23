package com.github.singularity.core.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.github.singularity.core.shared.DataStoreFileName
import net.harawata.appdirs.AppDirsFactory
import okio.Path.Companion.toPath
import org.koin.dsl.module
import java.io.File

actual val DataStoreModule = module {
    single {
        val directory = AppDirsFactory.getInstance()
            .getUserDataDir("Singularity", null, null)

        val file = File(directory, DataStoreFileName)
        file.parentFile?.mkdirs()
        
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { file.absolutePath.toPath() },
            scope = get(),
        )
    }
}
