package com.github.singularity.ui.feature.main


sealed interface MainIntent {
    data object ToDiscoverScreen : MainIntent
    data object ToBroadcastScreen : MainIntent
    data object ToSettingsScreen : MainIntent
    data object RefreshConnection : MainIntent
}
