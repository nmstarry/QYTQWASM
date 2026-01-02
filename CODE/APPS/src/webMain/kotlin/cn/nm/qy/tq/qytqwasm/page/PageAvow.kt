package cn.nm.qy.tq.qytqwasm.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.nm.qy.tq.qytqwasm.form.FORM
import cn.nm.qy.tq.qytqwasm.main.rememberWarnMent
import cn.nm.qy.tq.qytqwasm.mold.ViewMold
import cn.nm.qy.tq.qytqwasm.tool.sizedunt
import cn.nm.qy.tq.qytqwasm.unit.TextView
import cn.nm.qy.tq.qytqwasm.unit.VoidTier
import org.jetbrains.compose.resources.painterResource
import qytqwasm.apps.generated.resources.Res
import qytqwasm.apps.generated.resources.appsicon

@Composable
fun PageAvow(viewmold: ViewMold, callback: () -> Unit, finished: () -> Unit) {

    val appsdata by viewmold.gaindata.collectAsState()

    val warnview = rememberWarnMent()

    Column(
        modifier = Modifier.background(FORM.PURE).fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
    ) {
        VoidTier()

        Column(
            modifier = Modifier.padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier.clip(RoundedCornerShape(50.dp)).size(200.dp),
                    painter = painterResource(Res.drawable.appsicon),
                    contentDescription = null,
                )
            }
            TextView(
                fonttext = "轻语天气", fontwide = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier.padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically)
        ) {
            TextView(
                modifier = Modifier.padding(vertical = 10.dp), fonttext = "欢迎", fontsize = 20.sp, fontwide = FontWeight.Bold
            )
            TextView(
                fonttext = "在您开始使用我们的应用前\n请您仔细阅读并同意隐私政策和用户协议中的内容\n才能继续使用本应用及功能", aligning = TextAlign.Center, mostline = 10
            )
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
            ) {
                TextView(fonttext = "《用户协议》", modifier = Modifier.sizedunt {
                    warnview.show("还未编写")
                }.weight(1F).height(50.dp), aligning = TextAlign.Center, fontskin = Color(0XFF0080FF))
                TextView(fonttext = "《隐私策略》", modifier = Modifier.sizedunt {
                    warnview.show("还未编写")
                }.weight(1F).height(50.dp), aligning = TextAlign.Center, fontskin = Color(0XFF0080FF))
            }
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
            ) {
                TextView(fonttext = "拒绝", modifier = Modifier.sizedunt {
                    finished()
                }.background(FORM.CARD, FORM.QJYJ).weight(1F).height(50.dp), aligning = TextAlign.Center)
                TextView(fonttext = "同意", modifier = Modifier.sizedunt {
                    viewmold.savedata(appsdata.copy(accepted = true))
                    callback()
                }.background(FORM.CARD, FORM.QJYJ).weight(1F).height(50.dp), aligning = TextAlign.Center, fontskin = FORM.HOST)
            }
        }
    }
}