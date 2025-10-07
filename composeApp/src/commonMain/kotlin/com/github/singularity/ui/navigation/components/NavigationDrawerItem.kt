package com.github.singularity.ui.navigation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.navigation.Route
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.home
import singularity.composeapp.generated.resources.log
import singularity.composeapp.generated.resources.logs
import singularity.composeapp.generated.resources.settings

enum class NavigationDrawerItem(
    val label: StringResource,
    val icon: DrawableResource,
    val route: Any,
    val followsDivider: Boolean = false,
) {

    Home(
        icon = Res.drawable.home,
        label = Res.string.home,
        route = Route.Home,
    ),

    Settings(
        icon = Res.drawable.settings,
        label = Res.string.settings,
        route = Route.Settings,
        followsDivider = true,
    ),

    Logs(
        icon = Res.drawable.log,
        label = Res.string.logs,
        route = Route.Log,
    ),

}

@Composable
fun NavigationDrawerItem(
    item: NavigationDrawerItem,
    currentRoute: String?,
    closeDrawer: () -> Unit,
    navigateTo: (Any) -> Unit,
) {
    val colors = NavigationDrawerItemDefaults.colors()
    val selected = item.route::class.simpleName == currentRoute

    if (item.followsDivider) {
        HorizontalDivider(Modifier.padding(vertical = 4.dp))
    }

    Surface(
        selected = selected,
        onClick = {
            if (currentRoute != item.route::class.simpleName) navigateTo(item.route)
            closeDrawer()
        },
        color = colors.containerColor(selected).value,
        modifier = Modifier
            .semantics { role = Role.Tab }
            .fillMaxWidth()
            .height(40.dp),
        shape = RoundedCornerShape(4.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val selectedColor = MaterialTheme.colorScheme.primary
            Canvas(
                modifier = Modifier.width(3.dp)
                    .fillMaxHeight(0.75f),
            ) {
                if (selected) {
                    clipRect {
                        drawRoundRect(
                            color = selectedColor,
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(size.width),
                        )
                    }
                }
            }

            val iconColor = colors.iconColor(selected).value
            CompositionLocalProvider(
                value = LocalContentColor provides iconColor,
                content = {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        painter = item.icon.getPainter(),
                        contentDescription = item.label.getString(),
                    )
                },
            )
            Spacer(Modifier.width(12.dp))
            Box(Modifier.weight(1f)) {
                val labelColor = colors.textColor(selected).value
                CompositionLocalProvider(
                    value = LocalContentColor provides labelColor,
                    content = {
                        Text(
                            text = item.label.getString(),
                            fontSize = 14.sp,
                        )
                    }
                )
            }
//            if (badge != null) {
//                Spacer(Modifier.width(12.dp))
//                val badgeColor = colors.badgeColor(selected).value
//                CompositionLocalProvider(LocalContentColor provides badgeColor, content = badge)
//            }
        }

    }
}
