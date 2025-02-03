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

package com.google.jetstream.presentation.screens.movies

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.shim.Border
import com.google.jetstream.presentation.components.shim.CompactCard
import com.google.jetstream.presentation.components.shim.borderIndication
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamBorder
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamCardShape

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoviesScreenMovieList(
    modifier: Modifier = Modifier,
    movieList: List<Movie>,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    onMovieClick: (movie: Movie) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = movieList,
        label = "",
    ) { movieListTarget ->
        // ToDo: specify the pivot offset to 0.07f
        LazyRow(
            modifier = Modifier.focusRestorer(),
            contentPadding = PaddingValues(start = startPadding, end = endPadding)
        ) {
            items(movieListTarget) {
                MovieListItem(
                    itemWidth = 432.dp,
                    onMovieClick = onMovieClick,
                    movie = it,
                )
            }
        }
    }
}

@Composable
private fun MovieListItem(
    itemWidth: Dp,
    movie: Movie,
    modifier: Modifier = Modifier,
    onMovieClick: (movie: Movie) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    CompactCard(
        modifier = modifier
            .width(itemWidth)
            .aspectRatio(2f)
            .padding(end = 32.dp)
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
            .padding(top = JetStreamBorderWidth)
            .indication(
                interactionSource = interactionSource,
                indication = borderIndication(focused = JetStreamBorder)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        interactionSource = interactionSource,
        onClick = { onMovieClick(movie) },
        image = {
            val contentAlpha by animateFloatAsState(
                targetValue = if (isFocused) 1f else 0.5f,
                label = "",
            )
            AsyncImage(
                model = movie.posterUri,
                contentDescription = StringConstants
                    .Composable
                    .ContentDescription
                    .moviePoster(movie.name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = contentAlpha }
            )
        },
        title = {
            Column {
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier
                        .graphicsLayer { alpha = 0.6f }
                        .padding(start = 24.dp)
                )
                Text(
                    text = movie.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 24.dp
                    ),
                    // TODO: Remove this when CardContent is not overriding contentColor anymore
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}
