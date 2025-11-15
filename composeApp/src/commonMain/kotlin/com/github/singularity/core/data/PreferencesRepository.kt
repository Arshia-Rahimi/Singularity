package com.github.singularity.core.data

import androidx.compose.ui.graphics.Color
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.ThemeOption
import com.github.singularity.core.shared.model.PreferencesModel
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesRepository {
    
    val preferences: SharedFlow<PreferencesModel>

    suspend fun setAppThemeOption(themeOption: ThemeOption)

    suspend fun changePrimaryColor(color: Color)

    suspend fun setSyncMode(syncMode: SyncMode)

    suspend fun setScale(scale: Float)

}
