package com.google.jetstream.presentation.common

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.Role

fun Modifier.tvClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = tvClickable(null, null, enabled, onClickLabel, role, onClick)

fun Modifier.tvClickable(
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
    }.clickable(
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