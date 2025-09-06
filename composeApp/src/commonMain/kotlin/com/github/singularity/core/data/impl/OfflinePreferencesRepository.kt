package com.github.singularity.core.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.datastore.DataStoreModelSerializer
import com.github.singularity.core.datastore.PreferencesModel
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class OfflinePreferencesRepository(
    scope: CoroutineScope,
    private val dataStore: DataStore<Preferences>,
) : PreferencesRepository {
    
    override val preferences = dataStore.data
        .map { prefs ->
            prefs[stringPreferencesKey(DataStoreModelSerializer.KEY)]?.let {
                DataStoreModelSerializer.deserialize(it)
            } ?: PreferencesModel()
        }
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

    override suspend fun setAppTheme(theme: AppTheme) {
        preferences.first().copy(theme = theme).save()
    }

    override suspend fun setSyncMode(syncMode: SyncMode) {
        preferences.first().copy(syncMode = syncMode).save()
    }

    private suspend fun PreferencesModel.save() =
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(DataStoreModelSerializer.KEY)] =
                DataStoreModelSerializer.serialize(this)
        }
}
