/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.jetstream.presentation.screens.Screens

class DashboardState internal constructor(
    initialTopBarVisibility: Boolean = true,
    initialScreen: Screens = Screens.Home,
) {
    var isTopBarVisible by mutableStateOf(initialTopBarVisibility)
        private set

    var selectedScreen by mutableStateOf(initialScreen)
        private set

    var isTopBarFocused by mutableStateOf(false)

    fun showTopBar() {
        isTopBarVisible = true
    }

    fun hideTopBar() {
        isTopBarVisible = false
    }

    fun updateTopBarFocusState(hasFocus: Boolean) {
        isTopBarFocused = hasFocus
    }

    fun updateSelectedScreen(screen: Screens) {
        selectedScreen = screen
    }

    private fun snapshot(): Pair<Boolean, Int> {
        return isTopBarVisible to selectedScreen.toIndex()
    }

    companion object {
        val Saver = Saver<DashboardState, Pair<Boolean, Int>>(
            save = { it.snapshot() },
            restore = {
                val screen = Screens.fromIndex(it.second) ?: Screens.Home
                DashboardState(it.first, screen)
            }
        )
    }
}

@Composable
fun rememberDashboardState(
    initialIsVisibility: Boolean = true,
    initialScreen: Screens = Screens.Home,
) = rememberSaveable(saver = DashboardState.Saver) {
    DashboardState(initialIsVisibility, initialScreen)
}
