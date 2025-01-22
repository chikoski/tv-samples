package com.google.jetstream.presentation.components.tv

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

fun Modifier.tvSelectTarget(onSelect: () -> Unit): Modifier =
    onFocusChanged {
        if (it.isFocused) {
            onSelect()
        }
    }
