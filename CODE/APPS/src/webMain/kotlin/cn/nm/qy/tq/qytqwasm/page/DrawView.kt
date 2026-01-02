package cn.nm.qy.tq.qytqwasm.page

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

data class Body(
    val id: Int,
    val ys: Color,
    val dh: Animatable<Spot, AnimationVector3D>
)

data class Spot(
    val xz: Float = Random.nextFloat(),
    val yz: Float = Random.nextFloat(),
    val zz: Float = Random.nextFloat(),
)

val spot = TwoWayConverter<Spot, AnimationVector3D>(
    {
        AnimationVector3D(it.xz, it.yz, it.zz)
    }, {
        Spot(it.v1, it.v2, it.v3)
    }
)

@Composable
fun DrawBack(
    modifier: Modifier = Modifier,
    foreskip: Color = Color(0X20FFFFFF),
    backskip: Color = Color(0XFF80A0C0),
    loopvary: Boolean = true
) {
    val backskip by animateColorAsState(backskip, tween(1000))

    val list = remember {
        List(10) { loop ->
            Body(
                id = loop,
                ys = foreskip,
                dh = Animatable(Spot(), spot),
            )
        }
    }
    LaunchedEffect(Unit) {
        if (loopvary) {
            list.forEach { item ->
                launch {
                    while (true) {
                        item.dh.animateTo(
                            targetValue = Spot(),
                            animationSpec = spring(Random.nextFloat(), Random.nextFloat() * 10)
                        )
                    }
                }
            }
        }
    }
    Canvas(
        modifier = modifier
            .blur(radius = 100.dp)
            .fillMaxSize()
            .background(backskip)
    ) {
        list.forEach { item ->
            val viewsize = size
            val itemsize = min(viewsize.width, viewsize.height)

            val drawsize = max(1F, itemsize / 2 + item.dh.value.zz * itemsize / 2)

            val hx = item.dh.value.xz * viewsize.width
            val zx = item.dh.value.yz * viewsize.height / 2

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        item.ys,
                        Color(0X00000000)
                    ),
                    center = Offset(hx, zx),
                    radius = drawsize
                ),
                radius = drawsize,
                center = Offset(hx, zx)
            )
        }
        drawRect(Color(0X20000000))
    }
}