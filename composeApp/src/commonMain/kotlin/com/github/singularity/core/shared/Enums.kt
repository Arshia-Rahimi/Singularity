package com.github.singularity.core.shared

import org.jetbrains.compose.resources.StringResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.dark
import singularity.composeapp.generated.resources.follow_system
import singularity.composeapp.generated.resources.light

enum class AppTheme(
    val title: StringResource,
) {
    Light(Res.string.light),
    Dark(Res.string.dark),
    System(Res.string.follow_system),
}

enum class SyncMode {
    Server, Client,
}
