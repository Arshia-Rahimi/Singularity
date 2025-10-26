package com.github.singularity.ui.feature.home.client.pages.joinedgroup

interface JoinedGroupIntent {
    data object RefreshConnection : JoinedGroupIntent
    data object RemoveAllDefaults : JoinedGroupIntent
}
