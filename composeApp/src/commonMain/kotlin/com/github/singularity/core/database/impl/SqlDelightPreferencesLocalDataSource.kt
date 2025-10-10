package com.github.singularity.core.database.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.github.singularity.core.database.PreferencesLocalDataSource
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.database.toEnum
import com.github.singularity.core.shared.model.PreferencesModel
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

class SqlDelightPreferencesLocalDataSource(
    db: SingularityDatabase
) : PreferencesLocalDataSource {

    private val queries = db.preferencesQueries

    override val preferences = queries.get()
        .asFlow()
        .mapToOneOrNull(Dispatchers.IO)
        .map {
            it?.let {
                PreferencesModel(
                    theme = it.theme.toEnum(),
                    deviceId = it.deviceId,
                    appSecret = it.appSecret.toByteArray(),
                    syncMode = it.syncMode.toEnum(),
                    scale = it.scale.toEnum(),
                )
            }
        }

    override fun update(preferences: PreferencesModel) {
        queries.update(
            theme = preferences.theme.ordinal.toLong(),
            deviceId = preferences.deviceId,
            appSecret = preferences.appSecret.toString(),
            syncMode = preferences.syncMode.ordinal.toLong(),
            scale = preferences.scale.ordinal.toLong(),
        )
    }

    override fun insert(preferences: PreferencesModel) {
        queries.insert(
            theme = preferences.theme.ordinal.toLong(),
            deviceId = preferences.deviceId,
            appSecret = preferences.appSecret.toString(),
            syncMode = preferences.syncMode.ordinal.toLong(),
            scale = preferences.scale.ordinal.toLong(),
        )
    }

}
