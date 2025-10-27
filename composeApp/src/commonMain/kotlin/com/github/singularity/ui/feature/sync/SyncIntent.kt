package com.github.singularity.ui.feature.sync

interface SyncIntent {
    data object OpenDrawer : SyncIntent
    data object RefreshConnection : SyncIntent
}
