package com.google.jetstream.presentation.components.shim

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun ClassicCard(
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null, // ToDo: enable long click handler
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    contentPadding: PaddingValues = PaddingValues(),
    interactionSource: MutableInteractionSource? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    ) {
        Column(modifier = Modifier.padding(contentPadding)) {
            Box(contentAlignment = Alignment.Center, content = image)
            Column { CardContent(title = title, subtitle = subtitle, description = description) }
        }
    }
}