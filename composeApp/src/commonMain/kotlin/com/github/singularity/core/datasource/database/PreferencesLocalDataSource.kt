package com.github.singularity.core.datasource.database

import kotlinx.coroutines.flow.Flow

interface PreferencesLocalDataSource {

    val preferences: Flow<PreferencesModel?>

    fun update(preferences: PreferencesModel)

    fun insert(preferences: PreferencesModel)

}
