package com.google.jetstream.presentation.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun StandardCardContainer(
    imageCard: @Composable (interactionSource: MutableInteractionSource) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    contentColor: CardContainerColors = CardContainerDefaults.contentColor(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val focused by interactionSource.collectIsFocusedAsState()
    val pressed by interactionSource.collectIsPressedAsState()

    Column(modifier = modifier) {
        Box {
            imageCard(interactionSource)
        }
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardContainerContent(
                title = title,
                subtitle = subtitle,
                description = description,
                contentColor = contentColor.color(focused = focused, pressed = pressed)
            )
        }
    }
}

object CardContainerDefaults {
    /**
     * Creates [CardContainerColors] that represents the default content colors used in a
     * CardContainer.
     *
     * @param contentColor the default content color of this CardContainer.
     * @param focusedContentColor the content color of this CardContainer when focused.
     * @param pressedContentColor the content color of this CardContainer when pressed.
     */
    @ReadOnlyComposable
    @Composable
    fun contentColor(
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        focusedContentColor: Color = contentColor,
        pressedContentColor: Color = focusedContentColor
    ) =
        CardContainerColors(
            contentColor = contentColor,
            focusedContentColor = focusedContentColor,
            pressedContentColor = pressedContentColor
        )
}

@Immutable
class CardContainerColors
internal constructor(
    internal val contentColor: Color,
    internal val focusedContentColor: Color,
    internal val pressedContentColor: Color,
) {
    /** Returns the content color [Color] for different interaction states. */
    internal fun color(focused: Boolean, pressed: Boolean): Color {
        return when {
            focused -> focusedContentColor
            pressed -> pressedContentColor
            else -> contentColor
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CardContainerColors) return false

        if (this === other) return true

        if (contentColor != other.contentColor) return false
        if (focusedContentColor != other.focusedContentColor) return false
        if (pressedContentColor != other.pressedContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contentColor.hashCode()
        result = 31 * result + focusedContentColor.hashCode()
        result = 31 * result + pressedContentColor.hashCode()
        return result
    }

    override fun toString(): String {
        return "CardContainerContentColor(" +
                "contentColor=$contentColor, " +
                "focusedContentColor=$focusedContentColor, " +
                "pressedContentColor=$pressedContentColor)"
    }
}

@Composable
internal fun CardContainerContent(
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    contentColor: Color
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        CardContent(title, subtitle, description)
    }
}

private const val SubtitleAlpha = 0.6f
private const val DescriptionAlpha = 0.8f

@Composable
internal fun CardContent(
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {}
) {
    ProvideTextStyle(MaterialTheme.typography.titleMedium) { title.invoke() }
    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
        Box(Modifier.graphicsLayer { alpha = SubtitleAlpha }) { subtitle.invoke() }
    }
    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
        Box(Modifier.graphicsLayer { alpha = DescriptionAlpha }) { description.invoke() }
    }
}