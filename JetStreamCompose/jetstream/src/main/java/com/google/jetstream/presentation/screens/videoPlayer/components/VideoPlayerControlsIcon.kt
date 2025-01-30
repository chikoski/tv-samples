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

package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.components.shim.onSpaceBarPressed
import com.google.jetstream.presentation.components.shim.scaleIndication

@Composable
fun VideoPlayerControlsIcon(
    modifier: Modifier = Modifier,
    state: VideoPlayerState,
    isPlaying: Boolean,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused && isPlaying) {
        if (isFocused && isPlaying) {
            state.showControls()
        }
    }

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .scaleIndication(interactionSource)
            .onSpaceBarPressed(onClick), // ToDo: remove when Compose 1.8.0 is available
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        ),
        interactionSource = interactionSource
    ) {
        Icon(
            icon,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentDescription = contentDescription,
            tint = LocalContentColor.current
        )
    }
}
