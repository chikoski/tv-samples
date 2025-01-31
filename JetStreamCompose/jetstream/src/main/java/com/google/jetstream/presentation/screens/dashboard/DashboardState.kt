package com.google.jetstream.presentation.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.jetstream.presentation.screens.Screens

class DashboardState internal  constructor(
    initialTopBarVisibility: Boolean = true,
    initialScreen: Screens = Screens.Home,
) {
    var isTopBarVisible by mutableStateOf(initialTopBarVisibility)
        private set

    var selectedScreen by mutableStateOf(initialScreen)
        private set

    fun showTopBar() {
        isTopBarVisible = true
    }

    fun hideTopBar() {
        isTopBarVisible = false
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
    initialScreen: Screens = Screens.Home
) = rememberSaveable(saver = DashboardState.Saver) {
    DashboardState(initialIsVisibility, initialScreen)
}