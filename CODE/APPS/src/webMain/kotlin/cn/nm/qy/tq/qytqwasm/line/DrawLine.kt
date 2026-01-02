package cn.nm.qy.tq.qytqwasm.line

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.nm.qy.tq.qytqwasm.form.FORM

/*
绘制两段三次贝塞尔曲线的计算公式与推导端点切线：

在 P1 的切线 T1 = (P2 − P0) / 2
在 P2 的切线 T2 = (P3 − P1) / 2

C1 = P1 + T1/3 = P1 + (P2 − P0)/6
C2 = P2 − T2/3 = P2 − (P3 − P1)/6

在 P2 的切线 T2 = (P3 − P1) / 2
在 P3 的切线 T3 = (P4 − P2) / 2

C3 = P2 + T2/3 = P2 + (P3 − P1)/6
C4 = P3 − T3/3 = P3 − (P4 − P2)/6
*/

/*单条插值曲线绘制*/
class DrawManyStepOnes(linelist: List<Float>) {

    val starting = linelist.first()
    val finishes = linelist.last()
    val linelist = listOf(starting, starting) + linelist + listOf(finishes, finishes)
    val linemaxs = linelist.max()
    val linemins = linelist.min()
}

@Composable
fun DrawManyStepLine(
    modifier: Modifier,
    drawones: DrawManyStepOnes,
    loop: Int,
    gradient: List<Color> = listOf(
        Color(0XFFFFC040),
        Color(0XFF40C0FF),
    )
) {
    /*
    对于三次贝塞尔曲线的插值算法（满足G1曲线但不满足G2曲线）
    前一个点：P0
    曲线起点：P1
    控制点一：C1
    控制点二：C2
    曲线终点：P2
    后一个点：P3
    转换公式：

    C1 = P1 + (P2 - P0) / 6
    C2 = P2 - (P3 - P1) / 6
    */
    val measures = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .height(100.dp)
    ) {
        val pads = 10.dp.toPx()

        val high = size.height - pads * 2
        val wide = size.width / 4

        val yz00 =
            high - (drawones.linelist[loop + 0] - drawones.linemins) / (drawones.linemaxs - drawones.linemins) * high
        val yz01 =
            high - (drawones.linelist[loop + 1] - drawones.linemins) / (drawones.linemaxs - drawones.linemins) * high
        val yz02 =
            high - (drawones.linelist[loop + 2] - drawones.linemins) / (drawones.linemaxs - drawones.linemins) * high
        val yz03 =
            high - (drawones.linelist[loop + 3] - drawones.linemins) / (drawones.linemaxs - drawones.linemins) * high
        val yz04 =
            high - (drawones.linelist[loop + 4] - drawones.linemins) / (drawones.linemaxs - drawones.linemins) * high

        val yc01 = yz01 + (yz02 - yz00) / 6
        val yc02 = yz02 - (yz03 - yz01) / 6
        val yc03 = yz02 + (yz03 - yz01) / 6
        val yc04 = yz03 - (yz04 - yz02) / 6

        val xz00 = wide * 0
        val xz01 = wide * 1
        val xz02 = wide * 2
        val xz03 = wide * 3
        val xz04 = wide * 4

        val xc01 = xz01 + (xz02 - xz00) / 6
        val xc02 = xz02 - (xz03 - xz01) / 6
        val xc03 = xz02 + (xz03 - xz01) / 6
        val xc04 = xz03 - (xz04 - xz02) / 6

        val path = Path()
        path.moveTo(wide, yz01 + pads)
        path.cubicTo(
            xc01,
            yc01 + pads,

            xc02,
            yc02 + pads,

            wide * 2,
            yz02 + pads,
        )
        path.cubicTo(
            xc03,
            yc03 + pads,

            xc04,
            yc04 + pads,

            wide * 3,
            yz03 + pads,
        )

        drawLine(
            color = FORM.MASK,
            start = Offset(wide * 2, pads),
            end = Offset(wide * 2, high + pads),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
        )
        drawIntoCanvas { canvases ->
            canvases.saveLayer(
                bounds = size.toRect(),
                paint = Paint()
            )
            drawPath(
                path = path,
                color = FORM.DARK,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawRect(
                brush = Brush.verticalGradient(
                    colors = gradient
                ), blendMode = BlendMode.SrcIn
            )
            drawRect(
                FORM.DARK,
                topLeft = Offset.Zero.copy(x = wide),
                size = Size(5.dp.toPx(), size.height),
                blendMode = BlendMode.Clear
            )
            drawRect(
                FORM.DARK,
                topLeft = Offset.Zero.copy(x = wide - 5.dp.toPx()),
                size = Size(5.dp.toPx(), size.height),
                blendMode = BlendMode.Clear
            )
            canvases.restore()
        }

        drawCircle(
            color = FORM.DARK,
            center = Offset(wide * 2, yz02 + pads),
            radius = 4.dp.toPx()
        )
        drawCircle(
            color = FORM.PURE,
            center = Offset(wide * 2, yz02 + pads),
            radius = 2.dp.toPx()
        )

        val textgage = measures.measure(
            text = "${drawones.linelist[loop + 2]}℃",
            style = TextStyle(
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = FORM.PURE
            )
        )

        val position = if (yz02 + pads + textgage.size.height < high) {
            yz02 + pads + textgage.size.height
        } else {
            yz02 + pads - textgage.size.height * 2
        }

        drawText(
            textLayoutResult = textgage,
            topLeft = Offset(
                (size.width - textgage.size.width) / 2,
                position/*(size.height - textgage.size.height) / 2*/
            )
        )
    }
}

/*双条插值曲线绘制*/
class DrawManyStepBoth(
    highline: List<Float>,
    lowsline: List<Float>,
) {
    val highlinestarting = highline.first()
    var highlinefinishes = highline.last()
    val highlist = listOf(highlinestarting, highlinestarting) + highline + listOf(
        highlinefinishes,
        highlinefinishes
    )
    val lowslinestarting = lowsline.first()
    val lowslinefinishes = lowsline.last()
    val lowslist = listOf(lowslinestarting, lowslinestarting) + lowsline + listOf(
        lowslinefinishes,
        lowslinefinishes
    )
    val linemaxs = (highline + lowsline).max()
    val linemins = (highline + lowsline).min()
}

@Composable
fun DrawManyStepLine(
    modifier: Modifier,
    drawboth: DrawManyStepBoth,
    loop: Int,
    gradient: List<Color> = listOf(
        Color(0XFFFFC040),
        Color(0XFF40C0FF),
    )
) {

    /*
    对于三次贝塞尔曲线的插值算法（满足G1曲线但不满足G2曲线）
    前一个点：P0
    曲线起点：P1
    控制点一：C1
    控制点二：C2
    曲线终点：P2
    后一个点：P3
    转换公式：

    C1 = P1 + (P2 - P0) / 6
    C2 = P2 - (P3 - P1) / 6
    */

    val highlinemeasures = rememberTextMeasurer()
    val lowslinemeasures = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .padding(vertical = 30.dp)
            .height(100.dp)
    ) {
        val pads = 10.dp.toPx()

        val high = size.height - pads * 2
        val wide = size.width / 4

        val gl00 =
            high - (drawboth.highlist[loop + 0] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high
        val gl01 =
            high - (drawboth.highlist[loop + 1] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high
        val gl02 =
            high - (drawboth.highlist[loop + 2] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high
        val gl03 =
            high - (drawboth.highlist[loop + 3] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high
        val gl04 =
            high - (drawboth.highlist[loop + 4] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high

        val gc01 = gl01 + (gl02 - gl00) / 6
        val gc02 = gl02 - (gl03 - gl01) / 6
        val gc03 = gl02 + (gl03 - gl01) / 6
        val gc04 = gl03 - (gl04 - gl02) / 6

        val dl00 =
            high - (drawboth.lowslist[loop + 0] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high
        val dl01 =
            high - (drawboth.lowslist[loop + 1] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high
        val dl02 =
            high - (drawboth.lowslist[loop + 2] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high
        val dl03 =
            high - (drawboth.lowslist[loop + 3] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high
        val dl04 =
            high - (drawboth.lowslist[loop + 4] - drawboth.linemins) / (drawboth.linemaxs - drawboth.linemins) * high

        val dc01 = dl01 + (dl02 - dl00) / 6
        val dc02 = dl02 - (dl03 - dl01) / 6
        val dc03 = dl02 + (dl03 - dl01) / 6
        val dc04 = dl03 - (dl04 - dl02) / 6

        val xz00 = wide * 0
        val xz01 = wide * 1
        val xz02 = wide * 2
        val xz03 = wide * 3
        val xz04 = wide * 4

        val xc01 = xz01 + (xz02 - xz00) / 6
        val xc02 = xz02 - (xz03 - xz01) / 6
        val xc03 = xz02 + (xz03 - xz01) / 6
        val xc04 = xz03 - (xz04 - xz02) / 6

        val path = Path()
        path.moveTo(wide, gl01 + pads)
        path.cubicTo(
            xc01,
            gc01 + pads,

            xc02,
            gc02 + pads,

            wide * 2,
            gl02 + pads,
        )
        path.cubicTo(
            xc03,
            gc03 + pads,

            xc04,
            gc04 + pads,

            wide * 3,
            gl03 + pads,
        )
        path.moveTo(wide, dl01 + pads)
        path.cubicTo(
            xc01,
            dc01 + pads,

            xc02,
            dc02 + pads,

            wide * 2,
            dl02 + pads,
        )
        path.cubicTo(
            xc03,
            dc03 + pads,

            xc04,
            dc04 + pads,

            wide * 3,
            dl03 + pads,
        )

        drawLine(
            color = FORM.MASK,
            start = Offset(wide * 2, pads),
            end = Offset(wide * 2, high + pads),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
        )
        drawIntoCanvas { canvases ->
            canvases.saveLayer(
                bounds = size.toRect(),
                paint = Paint()
            )
            drawPath(
                path = path,
                color = FORM.DARK,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawRect(
                brush = Brush.verticalGradient(
                    colors = gradient
                ),
                blendMode = BlendMode.SrcIn
            )
            drawRect(
                FORM.DARK,
                topLeft = Offset.Zero.copy(x = wide),
                size = Size(5.dp.toPx(), size.height),
                blendMode = BlendMode.Clear
            )
            drawRect(
                FORM.DARK,
                topLeft = Offset.Zero.copy(x = wide - 5.dp.toPx()),
                size = Size(5.dp.toPx(), size.height),
                blendMode = BlendMode.Clear
            )
            canvases.restore()
        }
        drawCircle(
            color = FORM.DARK,
            center = Offset(wide * 2, gl02 + pads),
            radius = 4.dp.toPx()
        )
        drawCircle(
            color = FORM.PURE,
            center = Offset(wide * 2, gl02 + pads),
            radius = 2.dp.toPx()
        )
        drawCircle(
            color = FORM.DARK,
            center = Offset(wide * 2, dl02 + pads),
            radius = 4.dp.toPx()
        )
        drawCircle(
            color = FORM.PURE,
            center = Offset(wide * 2, dl02 + pads),
            radius = 2.dp.toPx()
        )

        val highlinetextgage = highlinemeasures.measure(
            text = "${drawboth.highlist[loop + 2]}℃",
            style = TextStyle(
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = FORM.PURE
            )
        )
        drawText(
            textLayoutResult = highlinetextgage,
            topLeft = Offset(
                (size.width - highlinetextgage.size.width) / 2,
                gl02 + pads - highlinetextgage.size.height * 2/*(size.height - textgage.size.height) / 2*/
            )
        )
        val lowslinetextgage = lowslinemeasures.measure(
            text = "${drawboth.lowslist[loop + 2]}℃",
            style = TextStyle(
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = FORM.PURE
            )
        )
        drawText(
            textLayoutResult = lowslinetextgage,
            topLeft = Offset(
                (size.width - lowslinetextgage.size.width) / 2,
                dl02 + pads + lowslinetextgage.size.height/*(size.height - textgage.size.height) / 2*/
            )
        )
    }
}