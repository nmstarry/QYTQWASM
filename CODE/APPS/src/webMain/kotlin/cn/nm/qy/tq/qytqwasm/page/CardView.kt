package cn.nm.qy.tq.qytqwasm.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import cn.nm.qy.tq.qytqwasm.form.FORM
import cn.nm.qy.tq.qytqwasm.unit.IconView
import cn.nm.qy.tq.qytqwasm.unit.TextView
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import qytqwasm.apps.generated.resources.Res
import qytqwasm.apps.generated.resources.directed
import kotlin.math.PI
import kotlin.math.min

@Composable
fun PageMark(
    realitem: Int = 2, listsize: Int = 5, viewsize: Dp = 5.dp
) {
    val viewwide = (viewsize * listsize) + 2.dp * (listsize - 1)
    Canvas(
        modifier = Modifier.size(viewwide, viewsize)
    ) {

        val dotssize = viewsize.toPx()

        repeat(listsize) {
            drawCircle(
                if (realitem == it) {
                    FORM.PURE
                } else {
                    FORM.MASK
                }, dotssize / 2.5F, Offset(
                    viewwide.toPx() * (it.toFloat() / listsize) + viewsize.toPx() / 2, dotssize / 2
                )
            )

        }
    }
}

/*风向示意*/
@Composable
fun WindFace(
    modifier: Modifier = Modifier,
    turnfate: Float = 0F,
    linewide: Dp = 10.dp,
) {
    Box(
        modifier = modifier.size(125.dp), contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val linespan = min(size.width, size.height) / 2 - linewide.toPx() / 2
            rotate(-360 / 64 / 8F) {
                val segments = (2F * PI * linespan / 64).toFloat()
                drawCircle(
                    color = FORM.MASK, radius = linespan, center = Offset(size.width / 2, size.height / 2), style = Stroke(
                        width = linewide.toPx(), pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(
                                segments * 0.25F, segments * (1 - 0.25F)
                            )
                        )
                    )
                )
            }
            rotate(-360 / 64 / 4F) {
                val directed = (2F * PI * linespan / 8).toFloat()
                drawCircle(
                    color = FORM.PURE, radius = linespan, center = Offset(size.width / 2, size.height / 2), style = Stroke(
                        width = linewide.toPx(), pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(
                                directed * 1 / 16F, directed * (1 - 1 / 16F)
                            )
                        )
                    )
                )
            }
        }
        listOf(
            "北" to Alignment.TopCenter, "南" to Alignment.BottomCenter, "西" to Alignment.CenterStart, "东" to Alignment.CenterEnd
        ).fastForEach {
            TextView(
                fonttext = it.first, fontsize = 10.sp, fontwide = FontWeight.Bold, modifier = Modifier.padding(20.dp).align(it.second), fontskin = FORM.PURE
            )
        }
        IconView(
            painterResource(Res.drawable.directed), modifier = Modifier.size(25.dp).rotate(turnfate), tintform = FORM.PURE
        )
    }
}

/*空气污染*/
@Composable
fun AirsArcs(
    modifier: Modifier = Modifier, progress: Float, linewide: Dp = 10.dp
) {
    Canvas(
        modifier = modifier.size(125.dp)
    ) {
        val linewide = linewide.toPx()
        rotate(90F) {
            drawArc(
                color = FORM.MASK, startAngle = 45F, sweepAngle = 270F, useCenter = false, topLeft = Offset(linewide / 2, linewide / 2), size = Size(size.width - linewide, size.height - linewide), style = Stroke(width = linewide, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    0.125F to Color(0XFF80C000),
                    0.375F to Color(0XFFFFC000),
                    0.525F to Color(0XFFFF8000),
                    0.675F to Color(0XFFFF4040),
                    0.825F to Color(0XFFC00000),
                    0.975F to Color(0XFF800000),
                ), startAngle = 45F, sweepAngle = 270F * progress, useCenter = false, topLeft = Offset(linewide / 2, linewide / 2), size = Size(size.width - linewide, size.height - linewide), style = Stroke(width = linewide, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

/*生活指数*/
@Composable
fun RowScope.LifeView(lifename: String, lifetext: String, lifeicon: DrawableResource) {
    Row(
        modifier = Modifier.weight(1F).background(FORM.MASK, FORM.QJYJ).padding(15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
            15.dp, Alignment.Start
        )
    ) {
        IconView(
            painterResource(lifeicon), modifier = Modifier.size(25.dp), tintform = FORM.PURE
        )
        Column(
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(
                10.dp, Alignment.CenterVertically
            )
        ) {
            TextView(fonttext = lifename, fontskin = FORM.PURE)
            TextView(fonttext = lifetext, fontskin = FORM.TINT)
        }
    }
}

/*污染类型*/
@Composable
fun AirsItem(
    name: String, text: String, data: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(end = 15.dp), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
        ) {
            TextView(
                fonttext = name,
                fontskin = FORM.PURE,
            )
            TextView(
                fonttext = text, fontskin = FORM.TINT, fontwide = FontWeight.Bold, fontsize = 10.sp
            )
        }
        TextView(
            fonttext = data.toString(),
            fontsize = 20.sp,
            fontskin = FORM.PURE,
        )
    }
}

/*日出日落*/
@Composable
fun SunsSite(
    progress: Float = 0.5F
) {
    Canvas(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp).fillMaxWidth().height(100.dp)
    ) {
        val path = Path()
        path.moveTo(0F, size.height)
        path.cubicTo(
            size.width / 4, size.height, size.width / 4, 0F, size.width / 2, 0F
        )
        path.cubicTo(
            size.width / 4 * 3, 0F, size.width / 4 * 3, size.height, size.width, size.height
        )
        drawPath(
            path = path, brush = Brush.horizontalGradient(
                colors = listOf(
                    FORM.MASK,
                    Color(0XFFFFC000),
                    FORM.MASK,
                )
            ), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        val measures = PathMeasure().also { it.setPath(path, false) }
        val position = measures.getPosition(measures.length * progress.coerceIn(0f, 1f))

        drawCircle(
            color = Color(0XFFFFFFFF),
            radius = 4.dp.toPx(),
            center = position,
        )
        drawCircle(
            color = Color(0XFFFFC000),
            radius = 2.dp.toPx(),
            center = position
        )
    }
}
