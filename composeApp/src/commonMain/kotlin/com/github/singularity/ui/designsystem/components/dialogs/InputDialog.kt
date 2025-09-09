package com.github.singularity.ui.designsystem.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.singularity.core.shared.compose.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.cancel

@Composable
fun InputDialog(
    visible: Boolean,
    title: String,
    confirmText: String,
    cancelText: String = Res.string.cancel.getString(),
    placeholder: String = "",
    initialValue: String = "",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
) {
    val confirm = { input: String ->
        onConfirm(input)
        onDismiss()
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(visible) {
        if (visible) focusRequester.requestFocus()
    }
    
    if (visible) {
        var inputValue by remember { mutableStateOf(initialValue) }

        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = { Text(placeholder) },
                        keyboardOptions = keyboardOptions,
                        keyboardActions = KeyboardActions(
                            onDone = { confirm(inputValue) },
                            onSend = { confirm(inputValue) },
                            onSearch = { confirm(inputValue) },
                            onGo = { confirm(inputValue) },
                            onNext = { confirm(inputValue) },
                            onPrevious = { confirm(inputValue) },
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onPreviewKeyEvent { keyEvent ->
                                if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                                    confirm(inputValue)
                                    true
                                } else {
                                    false
                                }
                            },
                        shape = MaterialTheme.shapes.large,
                        colors = TextFieldDefaults.colors(
                            cursorColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                cancelText,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { confirm(inputValue) }
                        ) {
                            Text(confirmText)
                        }
                    }
                }
            }
        }
    }
}
