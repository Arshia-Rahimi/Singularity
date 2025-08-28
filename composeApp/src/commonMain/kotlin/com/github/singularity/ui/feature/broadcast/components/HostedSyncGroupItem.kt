package com.github.singularity.ui.feature.broadcast.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.shared.compose.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.default
import singularity.composeapp.generated.resources.nodes_paired

@Composable
fun HostedSyncGroupItem(
    hostedSyncGroup: HostedSyncGroup,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            drawStyle = Stroke(width = 4f),
                        )
                    ) {
                        append(hostedSyncGroup.name)
                    }
                },
            )
            if (hostedSyncGroup.isDefault) {
                Text(
                    text = Res.string.default.getString(),
                    fontSize = 16.sp,
                )
            }
        }

        Text(
            text = "${hostedSyncGroup.nodes.size} ${Res.string.nodes_paired.getString()}",
            fontSize = 12.sp,
        )
    }
}
