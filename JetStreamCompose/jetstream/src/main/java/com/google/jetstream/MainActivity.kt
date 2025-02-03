/*
 * Copyright 2024 Google LLC
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

package com.google.jetstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.jetstream.presentation.App
import com.google.jetstream.presentation.components.shim.FormFactor
import com.google.jetstream.presentation.components.shim.UiMode
import com.google.jetstream.presentation.theme.JetStreamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (toEnableEdgeToEdge()) {
            enableEdgeToEdge()
        }

        setContent {
            JetStreamTheme {
                Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface
                    ) {
                        Box(
                            modifier = Modifier
                                .safeDrawingPadding()
                                .clipToBounds()
                        ) {
                            App(
                                onBackPressed = onBackPressedDispatcher::onBackPressed,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun toEnableEdgeToEdge(): Boolean {
        val uiMode = UiMode.from(resources.configuration)
        return when (uiMode.formFactor) {
            FormFactor.Normal -> true
            FormFactor.Desk -> true
            else -> false
        }
    }
}
