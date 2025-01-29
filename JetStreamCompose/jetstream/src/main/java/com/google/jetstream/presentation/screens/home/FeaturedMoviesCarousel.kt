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

package com.google.jetstream.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAll
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import com.google.jetstream.R
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamButtonShape
import com.google.jetstream.presentation.utils.Padding
import com.google.jetstream.presentation.utils.handleDPadKeyEvents
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalTvMaterial3Api::class)
internal class FeaturedCarouselState(
    private val itemCount: Int,
    initialActiveIndex: Int = 0
) {

    var carouselState by mutableStateOf(CarouselState(initialActiveIndex))
        private set

    val activeItemIndex: Int
        get() = carouselState.activeItemIndex

    fun moveToNextItem() {
        val updatedIndex = (activeItemIndex + 1) % itemCount
        carouselState = CarouselState(updatedIndex)
    }

    fun moveToPreviousItem() {
        val updatedIndex = (activeItemIndex - 1 + itemCount) % itemCount
        carouselState = CarouselState(updatedIndex)
    }

    companion object {
        val Saver = Saver<FeaturedCarouselState, Pair<Int, Int>>(
            save = { it.itemCount to it.activeItemIndex },
            restore = { FeaturedCarouselState(it.first, it.second) }
        )
    }

}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedMoviesCarousel(
    movies: List<Movie>,
    padding: Padding,
    goToVideoPlayer: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredCarouselState = rememberSaveable(movies, saver = FeaturedCarouselState.Saver) {
        FeaturedCarouselState(movies.size)
    }
    var isCarouselFocused by remember { mutableStateOf(false) }
    val alpha = if (isCarouselFocused) {
        1f
    } else {
        0f
    }

    Carousel(
        modifier = modifier
            .padding(start = padding.start, end = padding.start, top = padding.top)
            .border(
                width = JetStreamBorderWidth,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                shape = ShapeDefaults.Medium,
            )
            .clip(ShapeDefaults.Medium)
            .onFocusChanged {
                // Because the carousel itself never gets the focus
                isCarouselFocused = it.hasFocus
            }
            .semantics {
                contentDescription =
                    StringConstants.Composable.ContentDescription.MoviesCarousel
            }
            .handleDPadKeyEvents(onEnter = {
                goToVideoPlayer(movies[featuredCarouselState.activeItemIndex])
            })
            .dragDirectionDetector(featuredCarouselState)
        ,
        itemCount = movies.size,
        carouselState = featuredCarouselState.carouselState,
        carouselIndicator = {
            CarouselIndicator(
                itemCount = movies.size,
                activeItemIndex = featuredCarouselState.activeItemIndex,
            )
        },
        contentTransformStartToEnd = fadeIn(tween(durationMillis = 1000))
            .togetherWith(fadeOut(tween(durationMillis = 1000))),
        contentTransformEndToStart = fadeIn(tween(durationMillis = 1000))
            .togetherWith(fadeOut(tween(durationMillis = 1000))),
        content = { index ->
            val movie = movies[index]
            // background
            CarouselItemBackground(movie = movie, modifier = Modifier.fillMaxSize())
            // foreground
            CarouselItemForeground(
                movie = movie,
                isCarouselFocused = isCarouselFocused,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun BoxScope.CarouselIndicator(
    itemCount: Int,
    activeItemIndex: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(32.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            .graphicsLayer {
                clip = true
                shape = ShapeDefaults.ExtraSmall
            }
            .align(Alignment.BottomEnd)
    ) {
        CarouselDefaults.IndicatorRow(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            itemCount = itemCount,
            activeItemIndex = activeItemIndex,
        )
    }
}

@Composable
private fun CarouselItemForeground(
    movie: Movie,
    modifier: Modifier = Modifier,
    isCarouselFocused: Boolean = false
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = movie.name,
                style = MaterialTheme.typography.displayMedium.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 1
            )
            Text(
                text = movie.description,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.65f
                    ),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp)
            )
            AnimatedVisibility(
                visible = isCarouselFocused,
                content = {
                    WatchNowButton()
                }
            )
        }
    }
}

@Composable
private fun CarouselItemBackground(movie: Movie, modifier: Modifier = Modifier) {
    AsyncImage(
        model = movie.posterUri,
        contentDescription = StringConstants
            .Composable
            .ContentDescription
            .moviePoster(movie.name),
        modifier = modifier
            .drawWithContent {
                drawContent()
                drawRect(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
            },
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun WatchNowButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .padding(top = 8.dp),
        shape = JetStreamButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.watch_now),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

private fun Modifier.dragDirectionDetector(state: FeaturedCarouselState) =
    this then
            Modifier.pointerInput(state) {
                coroutineScope {
                    awaitEachGesture {
                        val downEvent =
                            awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                        var upEventOrCancellation: PointerInputChange? = null
                        while (upEventOrCancellation == null) {
                            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                            if (event.changes.fastAll { it.changedToUp() }) {
                                // All pointers are up
                                upEventOrCancellation = event.changes[0]
                            }
                        }

                        val horizontalDifference = (upEventOrCancellation.position - downEvent.position).x
                        if (horizontalDifference > 0) {
                            state.moveToNextItem()
                        } else {
                            state.moveToPreviousItem()
                        }
                    }
                }
            }