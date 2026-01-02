package cn.nm.qy.tq.qytqwasm.form

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.Font
import qytqwasm.apps.generated.resources.HarmonyOS_Sans_SC_Regular
import qytqwasm.apps.generated.resources.Res
import qytqwasm.apps.generated.resources.font

data object FORM {

    val PURE = Color(0XFFFFFFFF)/*白色*/
    val DARK = Color(0XFF000000)/*黑色*/

    val MAIN = Color(0X200080FF)/*主色*/
    val HOST = Color(0XFF0080FF)/*主色*/

    val CARD = Color(0X20808080)/*卡片*/
    val GRAY = Color(0XFF808080)/*灰色*/

    val WARN = Color(0XFFFF4040)/*警告*/
    val HEED = Color(0X20FF4040)/*注意*/

    val TINT = Color(0X80FFFFFF)/*浅色文字*/
    val MASK = Color(0X20FFFFFF)/*主页背景*/

    /*全局圆角*/
    val QJYJ = RoundedCornerShape(20.dp)

    @Composable
    fun FONT() = FontFamily(Font(Res.font.HarmonyOS_Sans_SC_Regular, FontWeight.Normal))

    @Composable
    fun FATE() = FontFamily(Font(Res.font.font, FontWeight.Normal, FontStyle.Normal))

}