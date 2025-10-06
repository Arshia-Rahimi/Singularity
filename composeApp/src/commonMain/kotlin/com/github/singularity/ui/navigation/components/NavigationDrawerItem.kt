package com.github.singularity.ui.navigation.components

import com.github.singularity.ui.navigation.Route
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.log
import singularity.composeapp.generated.resources.logs
import singularity.composeapp.generated.resources.settings

enum class NavigationDrawerItem(
    val label: StringResource,
    val icon: DrawableResource,
    val route: Any,
    val isFollowedByDivider: Boolean = false,
) {
    Settings(
        icon = Res.drawable.settings,
        label = Res.string.settings,
        route = Route.Settings,
    ),
    Logs(
        icon = Res.drawable.log,
        label = Res.string.logs,
        route = Route.Log,
    ),
}
