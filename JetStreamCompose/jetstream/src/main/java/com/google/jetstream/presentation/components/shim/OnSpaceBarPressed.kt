package com.google.jetstream.presentation.components.shim

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type

fun Modifier.onSpaceBarPressed(onPressed: () -> Unit): Modifier {
    return onKeyEvent {
        if (it.key == Key.Spacebar && it.type == KeyEventType.KeyUp) {
            onPressed()
            true
        } else {
            false
        }
    }
}