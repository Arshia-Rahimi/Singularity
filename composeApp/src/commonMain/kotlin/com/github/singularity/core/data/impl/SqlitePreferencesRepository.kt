package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.database.SqlitePreferencesDataSource
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.PreferencesModel
import com.github.singularity.core.shared.util.onFirst
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlin.io.encoding.Base64
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SqlitePreferencesRepository(
    private val preferencesDataSource: SqlitePreferencesDataSource,
) : PreferencesRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override val preferences = preferencesDataSource.preferences
        .onFirst {
            if (it != null) return@onFirst

            preferencesDataSource.insert(
                PreferencesModel(
                    deviceId = Uuid.Companion.random().toString(),
                    appSecret = Random.Default.nextBytes(32).let { secret ->
                        Base64.Default.withPadding(Base64.PaddingOption.ABSENT)
                            .encodeToByteArray(secret)
                    })
            )
           
        }
        .filterNotNull()
        .shareInWhileSubscribed(scope, 1)

    override suspend fun setAppTheme(theme: AppTheme) {
        preferencesDataSource.update(
            preferences.first().copy(theme = theme)
        )
    }

    override suspend fun setSyncMode(syncMode: SyncMode) {
        preferencesDataSource.update(
            preferences.first().copy(syncMode = syncMode)
        )
    }

}
