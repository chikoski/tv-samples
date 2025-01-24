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

package com.google.jetstream.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.jetstream.presentation.components.shim.Border
import com.google.jetstream.presentation.components.shim.StandardCardContainer
import com.google.jetstream.presentation.components.shim.borderIndication
import com.google.jetstream.presentation.components.shim.clickable
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamCardShape

@Composable
fun MovieCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    StandardCardContainer(
        modifier = modifier,
        title = title,
        imageCard = { interactionSource ->
            Box(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = borderIndication(
                        focusedBorder = Border(
                            stroke = BorderStroke(
                                width = JetStreamBorderWidth,
                                color = MaterialTheme.colorScheme.outline,
                            ),
                            shape = JetStreamCardShape
                        )
                    ),
                    onClick = onClick
                ),
                content = content
            )
        },
    )
}

