/*
 * Copyright 2023 Google LLC
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

package com.google.jetstream.presentation.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.components.shim.Border

val JetStreamCardShape = ShapeDefaults.ExtraSmall
val JetStreamButtonShape = ShapeDefaults.ExtraSmall
val IconSize = 20.dp
val JetStreamBorderWidth = 3.dp

/**
 * Space to be given below every Lazy (or scrollable) vertical list throughout the app
 */
val JetStreamBottomListPadding = 28.dp

val JetStreamBorder
    @Composable get() =
        Border(
            stroke = BorderStroke(
                width = JetStreamBorderWidth,
                color = MaterialTheme.colorScheme.onSurface
            ),
            shape = JetStreamCardShape
        )