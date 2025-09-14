package com.github.singularity.core.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.github.singularity.core.shared.model.PreferencesModel
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

class SqlitePreferencesDataSource(
    db: SingularityDatabase
) {

    private val queries = db.preferencesQueries

    val preferences = queries.get()
        .asFlow()
        .mapToOneOrNull(Dispatchers.IO)
        .map {
            it?.let {
                PreferencesModel(
                    theme = it.theme.toEnum(),
                    deviceId = it.deviceId,
                    appSecret = it.appSecret.toByteArray(),
                    syncMode = it.syncMode.toEnum(),
                )
            }
        }

    fun update(preferences: PreferencesModel) {
        queries.update(
            theme = preferences.theme.ordinal.toLong(),
            deviceId = preferences.deviceId,
            appSecret = preferences.appSecret.toString(),
            syncMode = preferences.syncMode.ordinal.toLong(),
        )
    }

    fun insert(preferences: PreferencesModel) {
        queries.insert(
            theme = preferences.theme.ordinal.toLong(),
            deviceId = preferences.deviceId,
            appSecret = preferences.appSecret.toString(),
            syncMode = preferences.syncMode.ordinal.toLong(),
        )
    }

}
