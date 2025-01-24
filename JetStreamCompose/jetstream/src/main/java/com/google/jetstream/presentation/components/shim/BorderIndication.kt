package com.google.jetstream.presentation.components.shim

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.components.ShapeTokens
import kotlinx.coroutines.launch

fun Modifier.borderIndication(
    interactionSource: InteractionSource,
    focusedBorder: Border = Border.None,
    pressedBorder: Border = focusedBorder,
    hoveredBorder: Border = focusedBorder,
): Modifier =
    indication(interactionSource, borderIndication(focusedBorder, pressedBorder, hoveredBorder))


fun borderIndication(
    focusedBorder: Border = Border.None,
    pressedBorder: Border = focusedBorder,
    hoveredBorder: Border = focusedBorder,
) = BorderIndicationNodeFactory(focusedBorder, pressedBorder, hoveredBorder)

class BorderIndicationNodeFactory(
    private val focusedBorder: Border = Border.None,
    private val pressedBorder: Border = focusedBorder,
    private val hoveredBorder: Border = focusedBorder,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return BorderIndicationNode(interactionSource, focusedBorder, pressedBorder, hoveredBorder)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BorderIndicationNodeFactory) return false
        return focusedBorder == other.focusedBorder &&
                pressedBorder == other.pressedBorder &&
                hoveredBorder == other.hoveredBorder
    }

    override fun hashCode(): Int {
        return focusedBorder.hashCode() * 31 +
                pressedBorder.hashCode() * 31 +
                hoveredBorder.hashCode()
    }

}

private class BorderIndicationNode(
    private val interactionSource: InteractionSource,
    private val focusedBorder: Border = Border.None,
    private val pressedBorder: Border = focusedBorder,
    private val hoveredBorder: Border = focusedBorder,
) : Modifier.Node(),
    DrawModifierNode {

    private var state: Border? = null

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                state = when (interaction) {
                    is PressInteraction.Press -> pressedBorder
                    is FocusInteraction.Focus -> focusedBorder
                    is HoverInteraction.Enter -> hoveredBorder
                    else -> null
                }
            }
            invalidateDraw()
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()

        val border = state
        if (border != null && border.stroke.width.value > 0) {
            val outline = border.shape.createOutline(size, layoutDirection, this)
            inset(inset = -border.inset.toPx()) {
                drawOutline(
                    outline = outline,
                    brush = border.stroke.brush,
                    style = Stroke(width = border.stroke.width.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Immutable
class Border(
    val stroke: BorderStroke,
    val inset: Dp = 0.dp,
    val shape: Shape = ShapeTokens.BorderDefaultShape
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Border

        if (stroke != other.stroke) return false
        if (inset != other.inset) return false
        if (shape != other.shape) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stroke.hashCode()
        result = 31 * result + inset.hashCode()
        result = 31 * result + shape.hashCode()
        return result
    }

    override fun toString(): String {
        return "Border(border=$stroke, inset=$inset, shape=$shape)"
    }

    fun copy(border: BorderStroke? = null, inset: Dp? = null, shape: Shape? = null): Border =
        Border(
            stroke = border ?: this.stroke,
            inset = inset ?: this.inset,
            shape = shape ?: this.shape
        )

    companion object {
        /**
         * Signifies the absence of a border. Use this if you do not want to display a border
         * indication in any of the TV Components.
         */
        val None =
            Border(
                stroke = BorderStroke(width = 0.dp, color = Color.Transparent),
                inset = 0.dp,
                shape = RectangleShape
            )
    }
}