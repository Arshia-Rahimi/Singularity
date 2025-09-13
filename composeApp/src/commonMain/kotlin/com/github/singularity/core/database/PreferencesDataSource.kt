package com.github.singularity.core.database

import com.github.singularity.core.datastore.PreferencesModel
import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {

    val preferences: Flow<PreferencesModel?>

    suspend fun update(preferences: PreferencesModel)

    suspend fun insert(preferences: PreferencesModel)

}
