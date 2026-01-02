package cn.nm.qy.tq.qytqwasm.unit

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.nm.qy.tq.qytqwasm.form.FORM
import cn.nm.qy.tq.qytqwasm.tool.sizedunt
import org.jetbrains.compose.resources.painterResource
import qytqwasm.apps.generated.resources.Res
import qytqwasm.apps.generated.resources.iconadds
import qytqwasm.apps.generated.resources.iconfind

/*文本显示*/
@Composable
fun TextView(
    modifier: Modifier = Modifier, fonttext: String = "", fontform: TextStyle = TextStyle.Default, fontfile: FontFamily? = null, fontskin: Color = Color(0XFF000000), fontsize: TextUnit = 15.sp, fontwide: FontWeight? = null, aligning: TextAlign = TextAlign.Start, mostline: Int = 1, overflow: TextOverflow = TextOverflow.Clip, autosize: TextAutoSize? = null
) {
    BasicText(
        text = fonttext, modifier = modifier.wrapContentHeight(Alignment.CenterVertically), style = fontform.merge(
        color = fontskin, fontSize = fontsize, textAlign = aligning, fontWeight = fontwide, fontFamily = fontfile ?: FORM.FONT()
    ), maxLines = mostline, color = { fontskin }, onTextLayout = { textinfo ->
        /*文本样式信息*/
    }, overflow = overflow, autoSize = autosize
    )
}

@Composable
fun TextView(
    modifier: Modifier = Modifier, fonttext: AnnotatedString, fontform: TextStyle = TextStyle.Default, fontfile: FontFamily? = null, fontskin: Color = Color(0XFF000000), fontsize: TextUnit = 15.sp, fontwide: FontWeight? = null, aligning: TextAlign = TextAlign.Start, mostline: Int = 1, overflow: TextOverflow = TextOverflow.Visible
) {
    BasicText(
        text = fonttext, modifier = modifier.wrapContentHeight(Alignment.CenterVertically).basicMarquee(
        iterations = Int.MAX_VALUE, repeatDelayMillis = 2000, initialDelayMillis = 2000
    ), style = fontform.merge(
        color = fontskin, fontSize = fontsize, textAlign = aligning, fontWeight = fontwide, fontFamily = fontfile ?: FORM.FONT()
    ), maxLines = mostline, color = { fontskin }, onTextLayout = { textinfo ->
        /*文本样式信息*/
    }, overflow = overflow
    )
}

/*文本输入编辑框框*/
@Composable
fun EditText(
    modifier: Modifier = Modifier,
    isinputs: Boolean = false,
    editiext: String = "",
    hinttext: String = "",
    aligning: TextAlign = TextAlign.Start,
    fontform: TextStyle = TextStyle.Default,
    fontskin: Color = Color(0XFF000000),
    fontsize: TextUnit = 15.sp,
    useropts: KeyboardOptions = KeyboardOptions(),
    useracts: KeyboardActions = KeyboardActions(),
    callback: (String) -> Unit = {},
) {

    val gainfoci = remember { FocusRequester() }

    var fielding by remember {
        mutableStateOf(
            TextFieldValue(
                editiext, TextRange(editiext.length)
            )
        )
    }

    LaunchedEffect(Unit) {
        if (isinputs) {
            withFrameNanos {
                gainfoci.requestFocus()
            }
        }
    }

    BasicTextField(
        modifier = modifier.wrapContentHeight(Alignment.CenterVertically).focusRequester(gainfoci),
        singleLine = true,
        textStyle = fontform.merge(
            color = fontskin, fontSize = fontsize, textAlign = aligning, fontFamily = FORM.FONT()
        ),
        cursorBrush = SolidColor(fontskin),
        value = fielding,
        onValueChange = {
            fielding = it
            callback(it.text)
        },
        decorationBox = { textarea ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                val voidtext by remember {
                    derivedStateOf {
                        fielding.text.isEmpty()
                    }
                }
                Box(
                    modifier = Modifier.weight(1F), contentAlignment = Alignment.CenterStart
                ) {
                    TextView(
                        modifier = Modifier.drawWithContent {
                            if (voidtext) {
                                drawContent()
                            }
                        }, fontskin = Color(0XFFA0A0A0), fonttext = hinttext, fontsize = fontsize, aligning = aligning, fontform = fontform
                    )
                    textarea()
                }
                val turnicon by animateFloatAsState(if (voidtext) 0F else 45F)
                IconView(
                    painterResource(
                        if (voidtext) {
                            Res.drawable.iconfind
                        } else {
                            Res.drawable.iconadds
                        }
                    ), modifier = Modifier.sizedunt(
                        enabling = !voidtext
                    ) {
                        fielding = fielding.copy("")
                        callback("")
                    }.rotate(turnicon).size(20.dp)
                )
            }
        },
        keyboardOptions = useropts,
        keyboardActions = useracts,
    )
}