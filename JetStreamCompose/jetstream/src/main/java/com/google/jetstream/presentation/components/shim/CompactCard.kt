package com.google.jetstream.presentation.components.shim

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun CompactCard(
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    colors: CardColors = CardDefaults.cardColors(),
    shape: Shape = CardDefaults.shape,
    scrimBrush: Brush = CardDefaults.scrimBrush,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Card(
        modifier = Modifier
            .clickable(interactionSource, indication = ripple(), onClick = onClick)
            .then(modifier),
        shape = shape,
        colors = colors,
    ) {
        Box(contentAlignment = Alignment.BottomStart) {
            Box(
                modifier = Modifier.drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush = scrimBrush)
                    }
                },
                contentAlignment = Alignment.Center,
                content = image
            )
            Column { CardContent(title = title, subtitle = subtitle, description = description) }
        }
    }
}

val CardDefaults.scrimBrush: Brush
    get() = Brush.verticalGradient(
        colors = listOf(
            Color(red = 28, green = 27, blue = 31, alpha = 0),
            Color(red = 28, green = 27, blue = 31, alpha = 204)
        )
    )