package com.github.singularity.core.data

import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.PreferencesModel
import com.github.singularity.ui.designsystem.theme.AppTheme
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesRepository {
    
    val preferences: SharedFlow<PreferencesModel>

    suspend fun setAppTheme(theme: AppTheme)

    suspend fun setSyncMode(syncMode: SyncMode)

    suspend fun setScale(scale: Float)

}
