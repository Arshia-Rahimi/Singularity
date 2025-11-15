package com.github.singularity.ui.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.connection
import singularity.composeapp.generated.resources.log
import singularity.composeapp.generated.resources.logs
import singularity.composeapp.generated.resources.permissions
import singularity.composeapp.generated.resources.plus
import singularity.composeapp.generated.resources.settings
import singularity.composeapp.generated.resources.test

interface NavigationDrawerItem {
    val label: StringResource
    val icon: DrawableResource
    val route: Any
}

enum class NavigationDrawerItemTop(
    override val label: StringResource,
    override val icon: DrawableResource,
    override val route: Any,
) : NavigationDrawerItem {

    Connection(
        icon = Res.drawable.connection,
        label = Res.string.connection,
	    route = Route.Connection,
    ),

    Permissions(
        icon = Res.drawable.permissions,
        label = Res.string.permissions,
        route = Route.Permissions,
    ),

    Test(
        icon = Res.drawable.plus,
        label = Res.string.test,
        route = Route.Test,
    ),

}

enum class NavigationDrawerItemBottom(
    override val label: StringResource,
    override val icon: DrawableResource,
    override val route: Any,
) : NavigationDrawerItem {

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

@Composable
fun NavigationDrawerItem(
    item: NavigationDrawerItem,
    currentRoute: String?,
    closeDrawer: () -> Unit,
    navigateTo: (Any) -> Unit,
) {
    val colors = NavigationDrawerItemDefaults.colors()
    val selected = item.route::class.simpleName == currentRoute

    Surface(
        selected = selected,
        onClick = {
            if (currentRoute != item.route::class.simpleName) navigateTo(item.route)
            closeDrawer()
        },
        color = colors.containerColor(selected).value,
        modifier = Modifier
            .semantics { role = Role.Tab }
            .fillMaxWidth(),
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
        }

    }
}
