package com.google.jetstream.presentation.components.shim

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable as originalClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.Role

fun Modifier.clickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = clickable(null, null, enabled, onClickLabel, role, onClick)

fun Modifier.clickable(
    interactionSource: MutableInteractionSource?,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier {
    return onKeyEvent { keyEvent ->
        if (enabled && isCenterButtonPressed(keyEvent)) {
            onClick()
            true
        } else {
            false
        }
    }.originalClickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
        role = role
    )
}

private fun isCenterButtonPressed(keyEvent: KeyEvent): Boolean {
    return keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionCenter
}