package com.github.singularity.core.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.github.singularity.core.shared.DATASTORE_FILENAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val DataStoreModule = module {
    single {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { androidContext().filesDir.resolve(DATASTORE_FILENAME).absolutePath.toPath() },
            scope = CoroutineScope(Dispatchers.IO),
        )
    }
}
