package com.github.singularity.ui.designsystem

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Composable
fun PainterIconButton(
    image: DrawableResource,
    contentDescription: StringResource,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            painter = image.getPainter(),
            contentDescription = contentDescription.getString(),
        )
    }
}
