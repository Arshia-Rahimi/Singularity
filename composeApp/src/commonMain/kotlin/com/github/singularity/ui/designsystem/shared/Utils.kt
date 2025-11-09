package com.github.singularity.ui.designsystem.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StringResource.getString(vararg formatArgs: Any) = stringResource(this, *formatArgs)

@Composable
fun DrawableResource.getPainter() = painterResource(this)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoRippleTextButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	shape: Shape = ButtonDefaults.textShape,
	colors: ButtonColors = ButtonDefaults.textButtonColors(),
	elevation: ButtonElevation? = null,
	border: BorderStroke? = null,
	contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
	interactionSource: MutableInteractionSource? = null,
	content: @Composable RowScope.() -> Unit
) {
	CompositionLocalProvider(LocalRippleConfiguration provides null) {
		TextButton(
			onClick = onClick,
			modifier = modifier,
			enabled = enabled,
			shape = shape,
			colors = colors,
			elevation = elevation,
			border = border,
			contentPadding = contentPadding,
			interactionSource = interactionSource,
			content = content,
		)
	}
}
