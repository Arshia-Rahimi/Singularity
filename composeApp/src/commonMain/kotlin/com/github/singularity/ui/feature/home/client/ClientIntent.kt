package com.github.singularity.ui.feature.home.client

import com.github.singularity.core.shared.model.JoinedSyncGroup


sealed interface ClientIntent {
    data object ToggleSyncMode : ClientIntent
    data object OpenDrawer : ClientIntent
    data class SetAsDefault(val group: JoinedSyncGroup) : ClientIntent
}
