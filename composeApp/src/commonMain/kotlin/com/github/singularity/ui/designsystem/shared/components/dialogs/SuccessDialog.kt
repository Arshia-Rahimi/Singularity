package com.github.singularity.ui.designsystem.shared.components.dialogs

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun SuccessDialog(
    visible: Boolean,
    message: String,
    successIcon: Painter,
    autoDismissDuration: Long = 2000,
    onDismiss: () -> Unit
) {
    FlashDialog(
        visible = visible,
        message = message,
        duration = autoDismissDuration,
        icon = {
            Icon(
                painter = successIcon,
                contentDescription = "Success",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(40.dp)
            )
        },
        onDismiss = onDismiss
    )
}
