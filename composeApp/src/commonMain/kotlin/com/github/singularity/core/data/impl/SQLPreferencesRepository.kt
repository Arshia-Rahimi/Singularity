package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.database.PreferencesDataSource
import com.github.singularity.core.datastore.PreferencesModel
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.util.onFirst
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class SQLPreferencesRepository(
    private val preferencesDataSource: PreferencesDataSource,
) : PreferencesRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override val preferences = preferencesDataSource.preferences
        .onFirst {
            if (it == null) {
                preferencesDataSource.insert(PreferencesModel())
            }
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