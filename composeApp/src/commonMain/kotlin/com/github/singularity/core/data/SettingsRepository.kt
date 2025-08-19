package com.github.singularity.core.data

import com.github.singularity.core.datastore.Settings
import com.github.singularity.core.shared.AppTheme
import kotlinx.coroutines.flow.SharedFlow

interface SettingsRepository {
    val settings: SharedFlow<Settings>

    suspend fun setAppTheme(theme: AppTheme)

}
