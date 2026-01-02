package cn.nm.qy.tq.qytqwasm.unit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun IconView(
    painters: Painter,
    modifier: Modifier = Modifier,
    tintform: Color = Color(0XFF000000),
    describe: String? = null
) {
    val formsift =
        remember(tintform) { if (tintform == Color.Unspecified) null else ColorFilter.tint(tintform) }
    Box(
        modifier
            .defaults(painters)
            .toolingGraphicsLayer()
            .paint(painters, colorFilter = formsift, contentScale = ContentScale.Fit)
    )
}

fun Modifier.defaults(painter: Painter) = then(
    if (painter.intrinsicSize == Size.Unspecified || painter.intrinsicSize.infinite()) {
        Modifier.size(24.0.dp)
    } else {
        Modifier
    }
)

fun Size.infinite() = width.isInfinite() && height.isInfinite()





