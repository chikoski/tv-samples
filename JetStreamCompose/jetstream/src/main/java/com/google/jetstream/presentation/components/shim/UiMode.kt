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

package com.google.jetstream.presentation.components.shim

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

enum class FormFactor {
    Undefined,
    Normal,
    Desk,
    Car,
    Tv,
    Appliance,
    Watch,
    VRHeadset,
}

enum class NightMode {
    Undefined,
    Yes,
    No,
}

class UiMode(val formFactor: FormFactor, val nightMode: NightMode) {

    companion object {
        fun from(configuration: Configuration): UiMode = from(configuration.uiMode)

        private fun from(uiMode: Int): UiMode {
            val nightModeIntValue = uiMode and Configuration.UI_MODE_NIGHT_MASK
            val nightMode = nightMode(nightModeIntValue)
            val formFactor = formFactor(uiMode - nightModeIntValue)
            return UiMode(formFactor, nightMode)
        }

        private fun nightMode(value: Int): NightMode {
            return when (value) {
                Configuration.UI_MODE_NIGHT_YES -> NightMode.Yes
                Configuration.UI_MODE_NIGHT_NO -> NightMode.No
                else -> NightMode.Undefined
            }
        }

        private fun formFactor(value: Int): FormFactor {
            return when (value) {
                Configuration.UI_MODE_TYPE_NORMAL -> FormFactor.Normal
                Configuration.UI_MODE_TYPE_DESK -> FormFactor.Desk
                Configuration.UI_MODE_TYPE_CAR -> FormFactor.Car
                Configuration.UI_MODE_TYPE_TELEVISION -> FormFactor.Tv
                Configuration.UI_MODE_TYPE_APPLIANCE -> FormFactor.Appliance
                Configuration.UI_MODE_TYPE_WATCH -> FormFactor.Watch
                Configuration.UI_MODE_TYPE_VR_HEADSET -> FormFactor.VRHeadset
                else -> FormFactor.Undefined
            }
        }
    }
}

@Composable
fun rememberUiMode(): UiMode {
    val configuration = LocalConfiguration.current
    return remember(configuration.uiMode) {
        UiMode.from(configuration)
    }
}
