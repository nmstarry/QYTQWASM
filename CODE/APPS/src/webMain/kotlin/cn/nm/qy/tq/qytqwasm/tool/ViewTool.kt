package cn.nm.qy.tq.qytqwasm.tool

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*fun Modifier.rippling(
    isbounds: Boolean = false,
    enabling: Boolean = true,
    onclicks: () -> Unit = {},
) = clickable(
    interactionSource = MutableInteractionSource(),
    indication = null,
    onClick = onclicks,
    enabled = enabling,
)*/

fun Modifier.onclicks(
    enabling: Boolean = true,
    onclicks: () -> Unit = {},
) = clickable(
    interactionSource = null,
    indication = null,
    onClick = onclicks,
    enabled = enabling,
)

fun Modifier.sizedunt(
    enabling: Boolean = true,
    onclicks: () -> Unit = {},
) = clickable(
    interactionSource = null,
    indication = SizeFall(),
    onClick = onclicks,
    enabled = enabling,
)

fun Modifier.duntsets(
    enabling: Boolean = true,
    interact: MutableInteractionSource? = null,
    indicant: Indication? = SizeFall(),
    onclicks: () -> Unit = {},
    longdunt: (() -> Unit)? = null,
    dualdunt: (() -> Unit)? = null
) = combinedClickable(
    interactionSource = interact,
    indication = indicant,
    enabled = enabling,
    onLongClick = longdunt,
    onDoubleClick = dualdunt,
    onClick = onclicks
)

fun Modifier.pointers(
    keysmark: Any? = Unit,
    initiate: AwaitPointerEventScope.(Offset) -> Unit = {},
    dragging: AwaitPointerEventScope.(Offset) -> Unit = {},
    finishes: AwaitPointerEventScope.(Offset) -> Unit = {},
) = pointerInput(keysmark) {
    awaitPointerEventScope {
        while (true) {
            val pointers = awaitPointerEvent()
            val dragmove = pointers.changes.first()
            when {
                dragmove.pressed && !dragmove.previousPressed -> { /*按下*/
                    initiate(dragmove.position - dragmove.previousPosition)
                }

                dragmove.pressed && dragmove.previousPressed -> { /*拖动*/
                    dragging(dragmove.position - dragmove.previousPosition)
                }

                !dragmove.pressed && dragmove.previousPressed -> { /*抬起*/
                    finishes(dragmove.position - dragmove.previousPosition)
                }
            }
            dragmove.consume()
        }
    }
}

fun Modifier.pointers(
    keysmark: Any? = Unit,
    callback: AwaitPointerEventScope.(position:/*位置*/ Offset, initiate:/*按下*/ Boolean, dragging:/*拖动*/ Boolean, finishes:/*抬起*/ Boolean) -> Unit,
) = pointerInput(keysmark) {
    awaitPointerEventScope {
        while (true) {
            val pointers = awaitPointerEvent()
            val dragmove = pointers.changes.first()
            callback(
                dragmove.position - dragmove.previousPosition,
                dragmove.pressed && !dragmove.previousPressed,
                dragmove.pressed && dragmove.previousPressed,
                !dragmove.pressed && dragmove.previousPressed
            )
            dragmove.consume()
        }
    }
}

fun Modifier.pointers(
    keysmark: Any? = Unit,
    callback: AwaitPointerEventScope.(position: Offset) -> Unit,
) = pointerInput(keysmark) {
    awaitPointerEventScope {
        while (true) {
            val pointers = awaitPointerEvent()
            if (pointers.changes.any { it.pressed }) {
                val dragmove = pointers.changes.first()
                callback(dragmove.position)
                dragmove.consume()
            }
        }
    }
}

fun Modifier.sitedrag(
    keysmark: Any? = Unit,
    callback: PointerInputScope.(Float, Boolean) -> Unit,
) = composed {
    var finishes by remember { mutableFloatStateOf(0F) }
    pointerInput(keysmark) {
        detectHorizontalDragGestures(
            {
                callback(finishes, false)
            },
            {
                callback(finishes, true)
            },
            {
                callback(finishes, true)
            }) { site, drag ->
            finishes = drag
            callback(drag, false)
        }
    }
}

data class SizeFall(val form: Shape = CircleShape) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return VaryDown(interactionSource)
    }
}

class VaryDown(val interact: InteractionSource) : Modifier.Node(), DrawModifierNode {
    val animates = Animatable(1F)
    var viewdown: Job? = null
    var viewlift: Job? = null
    fun viewdown() {
        viewlift?.cancel()
        viewdown?.cancel()
        viewdown = coroutineScope.launch {
            animates.animateTo(0.8F, spring())
        }
    }

    fun viewlift() {
        viewlift = coroutineScope.launch {
            viewdown?.join()
            animates.animateTo(1F, spring())
        }
    }

    override fun onAttach() {
        coroutineScope.launch {
            interact.interactions.collectLatest {
                if (it is PressInteraction.Press) {
                    viewdown()
                } else {
                    viewlift()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(
            scale = animates.value,
        ) {
            this@draw.drawContent()
        }
    }
}