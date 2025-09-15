package com.github.singularity.core.database

import com.github.singularity.core.shared.model.PreferencesModel
import kotlinx.coroutines.flow.Flow

interface PreferencesLocalDataSource {

    val preferences: Flow<PreferencesModel?>

    fun update(preferences: PreferencesModel)

    fun insert(preferences: PreferencesModel)

}
