package com.github.singularity.core.shared

import androidx.compose.ui.graphics.Color
import com.github.singularity.ui.feature.settings.components.DefaultPrimaryColor
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.amoled
import singularity.composeapp.generated.resources.client
import singularity.composeapp.generated.resources.dark
import singularity.composeapp.generated.resources.follow_system
import singularity.composeapp.generated.resources.light
import singularity.composeapp.generated.resources.server

enum class SyncMode(
	val title: StringResource,
) {
	Client(Res.string.client),
	Server(Res.string.server),
}

@Serializable
data class AppTheme(
    val themeOption: ThemeOption = ThemeOption.System,
    val customPrimaryColorULong: ULong = DefaultPrimaryColor.value,
) {
    val customPrimaryColor: Color
        get() = Color(customPrimaryColorULong)
}

enum class ThemeOption(
	val title: StringResource,
) {
	Light(Res.string.light),
	Dark(Res.string.dark),
    Amoled(Res.string.amoled),
	System(Res.string.follow_system)
}
