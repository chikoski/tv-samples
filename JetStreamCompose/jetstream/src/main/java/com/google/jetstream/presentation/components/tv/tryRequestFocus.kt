package com.google.jetstream.presentation.components.tv

import androidx.compose.ui.focus.FocusRequester

fun FocusRequester.tryRequestFocus(): Result<Unit> {
    return try {
        this.requestFocus()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}