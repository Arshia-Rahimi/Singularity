package com.github.singularity.core.database.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.github.singularity.core.database.PreferencesDataSource
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datastore.PreferencesModel
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

class LocalPreferencesDataSource(
    db: SingularityDatabase
) : PreferencesDataSource {

    private val queries = db.preferencesQueries

    override val preferences = queries.get()
        .asFlow()
        .mapToOneOrNull(Dispatchers.IO)
        .map {
            it?.let {
                PreferencesModel(
                    theme = AppTheme.entries[it.theme.toInt()],
                    deviceId = it.deviceId,
                    appSecret = it.appSecret.toByteArray(),
                    syncMode = SyncMode.entries[it.theme.toInt()],
                )
            }
        }

    override suspend fun update(preferences: PreferencesModel) {
        queries.update(
            theme = preferences.theme.ordinal.toLong(),
            deviceId = preferences.deviceId,
            appSecret = preferences.appSecret.toString(),
            syncMode = preferences.syncMode.ordinal.toLong(),
        )
    }

    override suspend fun insert(preferences: PreferencesModel) {
        queries.insert(
            theme = preferences.theme.ordinal.toLong(),
            deviceId = preferences.deviceId,
            appSecret = preferences.appSecret.toString(),
            syncMode = preferences.syncMode.ordinal.toLong(),
        )
    }

}
