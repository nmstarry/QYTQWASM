package cn.nm.qy.tq.qytqwasm.form

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.nm.qy.tq.qytqwasm.mold.ViewMold

/*形状样式*/
@Immutable
data class HormForm(
    var capeskew: CornerBasedShape = CutCornerShape(25.dp),/*普通斜角*/
    var capering: CornerBasedShape = RoundedCornerShape(25.dp),/*普通圆角*/
    var deadskew: CornerBasedShape = AbsoluteCutCornerShape(25.dp),/*绝对斜角*/
    var deadring: CornerBasedShape = AbsoluteRoundedCornerShape(25.dp),/*绝对圆角*/
)

/*色彩风格*/
@Immutable
data class ViewFrom(
    var backlead: Color = Color(0XFFFFFFFF),
    var backtrim: Color = Color(0XFFFFFFFF),

    var forelead: Color = Color(0XFF000000),
    var foretrim: Color = Color(0XFF000000),
)

/*字体风格*/
@Immutable
data class FontForm(
    var headtext: TextUnit = 20.sp,
    var bodytext: TextUnit = 15.sp,
)

/*风格合集*/
@Immutable
data class OwnsForm(
    var viewform: ViewFrom = ViewFrom(),
    var fontform: FontForm = FontForm(),
    var hormform: HormForm = HormForm(),
)

/*全局主题*/
@Composable
fun MainForm(
    darkmode: Boolean = false,
    isdarkui: Boolean = isSystemInDarkTheme(),
    contents: @Composable (ViewMold) -> Unit
) {

    val viewmold = viewModel{ ViewMold() }/*val viewmold = viewModel<ViewMold>()*/

    val ownsform by remember {
        derivedStateOf {
            OwnsForm(
                viewform = if (darkmode && isdarkui) {
                    ViewFrom(
                        /*夜间*/
                        backlead = Color(0XFF000000),
                        backtrim = Color(0XFF202020),

                        forelead = Color(0XFFFFFFFF),
                        foretrim = Color(0XFFE0E0E0),
                    )
                } else {
                    ViewFrom(
                        /*白天*/
                        backlead = Color(0XFFFFFFFF),
                        backtrim = Color(0XFFE0E0E0),

                        forelead = Color(0XFF000000),
                        foretrim = Color(0XFF202020),
                    )
                }
            )
        }
    }
    CompositionLocalProvider(
        locality provides ownsform,
        LocalTextSelectionColors provides TextSelectionColors(Color(0XFF808080), Color(0XFF808080)),
    ) {
        contents(viewmold)
    }
}

/*全局提供*/
internal val locality = staticCompositionLocalOf { OwnsForm() }/*静态数据*/

internal val localitymainform = compositionLocalOf { OwnsForm() }/*重组刷新*/

/*属性访问*/
object MainForm {
    val ownsform: OwnsForm
        @Composable @ReadOnlyComposable get() = locality.current
}