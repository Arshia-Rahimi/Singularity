package com.github.singularity.core.data

import com.github.singularity.core.datastore.PreferencesModel
import com.github.singularity.core.shared.AppTheme
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesRepository {
    
    val preferences: SharedFlow<PreferencesModel>

    suspend fun setAppTheme(theme: AppTheme)

}
