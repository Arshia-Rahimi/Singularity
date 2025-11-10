package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.database.PreferencesLocalDataSource
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.PreferencesModel
import com.github.singularity.core.shared.util.onFirst
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlin.io.encoding.Base64
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SqlitePreferencesRepository(
    private val preferencesLocalDataSource: PreferencesLocalDataSource,
) : PreferencesRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override val preferences = preferencesLocalDataSource.preferences
        .onFirst {
	        if (it == null) return@onFirst

            preferencesLocalDataSource.insert(
                PreferencesModel(
                    deviceId = Uuid.random().toString(),
                    appSecret = Random.nextBytes(32).let { secret ->
                        Base64.withPadding(Base64.PaddingOption.ABSENT)
                            .encodeToByteArray(secret)
                    }
                )
            )

        }
        .filterNotNull()
        .distinctUntilChanged()
        .shareInWhileSubscribed(scope, 1)

    override suspend fun setAppTheme(theme: AppTheme) {
        preferencesLocalDataSource.update(
            preferences.first().copy(theme = theme)
        )
    }

    override suspend fun setSyncMode(syncMode: SyncMode) {
        preferencesLocalDataSource.update(
            preferences.first().copy(syncMode = syncMode)
        )
    }

    override suspend fun setScale(scale: Float) {
        preferencesLocalDataSource.update(
            preferences.first().copy(scale = scale)
        )
    }

}
