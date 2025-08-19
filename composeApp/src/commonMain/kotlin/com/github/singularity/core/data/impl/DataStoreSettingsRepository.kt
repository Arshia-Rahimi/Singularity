package com.github.singularity.core.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.singularity.core.data.SettingsRepository
import com.github.singularity.core.datastore.Settings
import com.github.singularity.core.datastore.SettingsSerializer
import com.github.singularity.core.shared.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class DataStoreSettingsRepository(
    scope: CoroutineScope,
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    override val settings = dataStore.data
        .map { prefs ->
            prefs[stringPreferencesKey(SettingsSerializer.KEY)]?.let {
                SettingsSerializer.deserialize(it)
            } ?: Settings()
        }
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

    override suspend fun setAppTheme(theme: AppTheme) {
        settings.first().copy(theme = theme).save()
    }

    private suspend fun Settings.save() =
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(SettingsSerializer.KEY)] =
                SettingsSerializer.serialize(this)
        }
}
