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

enum class ScaleOption(
    val value: Float,
    val label: String,
) {
    S50(0.5f, "50%"),
    S62_5(0.625f, "62.5%"),
    S75(0.75f, "75%"),
    S87_5(0.875f, "87.5%"),
    S100(1f, "100%"),
    S112_5(1.125f, "112.5%"),
    S125(1.125f, "125%"),
    S137_5(1.375f, "137.5%"),
    S150(1.5f, "150%"),
    S162_5(1.625f, "162.5%"),
    S175(1.75f, "175%"),
    S187_5(1.875f, "187.5%"),
    S200(2f, "200%"),
}
