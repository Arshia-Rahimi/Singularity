package com.github.singularity.core.data

import com.github.singularity.core.datastore.PreferencesModel
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesRepository {
    
    val preferences: SharedFlow<PreferencesModel>

    suspend fun setAppTheme(theme: AppTheme)

    suspend fun setSyncMode(syncMode: SyncMode)

}
