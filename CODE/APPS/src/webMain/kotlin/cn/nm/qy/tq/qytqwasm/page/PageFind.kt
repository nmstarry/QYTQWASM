package cn.nm.qy.tq.qytqwasm.page

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import cn.nm.qy.tq.qytqwasm.cytq.CytqCity
import cn.nm.qy.tq.qytqwasm.cytq.GainCity
import cn.nm.qy.tq.qytqwasm.cytq.GainMent
import cn.nm.qy.tq.qytqwasm.form.FORM
import cn.nm.qy.tq.qytqwasm.main.rememberWarnMent
import cn.nm.qy.tq.qytqwasm.mold.ViewMold
import cn.nm.qy.tq.qytqwasm.tool.HttpTool
import cn.nm.qy.tq.qytqwasm.tool.sizedunt
import cn.nm.qy.tq.qytqwasm.unit.EditText
import cn.nm.qy.tq.qytqwasm.unit.IconView
import cn.nm.qy.tq.qytqwasm.unit.TextView
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import qytqwasm.apps.generated.resources.Res
import qytqwasm.apps.generated.resources.iconrest

@Composable
fun PageFind(
    viewmold: ViewMold, addscity: (CytqCity.Places) -> Unit
) {
    val warnview = rememberWarnMent()

    val appsdata by viewmold.gaindata.collectAsState()

    val controls = LocalSoftwareKeyboardController.current
    val onscopes = rememberCoroutineScope()

    var findcity by rememberSaveable { mutableStateOf("") }
    var gainlist by rememberSaveable { mutableStateOf(GainCity()) }

    fun cansadds(callbacl: () -> Unit) {
        if (appsdata.cytqlist.size <= 20) {
            callbacl()
        } else {
            warnview.show("最多只能添加二十个城市")
        }
    }

    LaunchedEffect(Unit) {
        controls?.show()
    }

    Column(
        modifier = Modifier.background(FORM.PURE).fillMaxSize().systemBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top
    ) {
        EditText(
            modifier = Modifier.padding(15.dp).background(FORM.MAIN, FORM.QJYJ).padding(15.dp),
            editiext = findcity,
            hinttext = "搜索城市",
            isinputs = true,
            useropts = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            useracts = KeyboardActions(onSearch = {
                if (findcity.isNotBlank()) {
                    gainlist = GainCity(GainMent.LOAD)
                    controls?.hide()
                    onscopes.launch {
                        HttpTool.gaincity(findcity) {
                            gainlist = it
                        }
                    }
                } else {
                    warnview.show("搜索内容不能为空")
                }
            }),
        ) {
            findcity = it
            gainlist = GainCity(GainMent.STOP)
        }
        AnimatedVisibility(
            gainlist.gainment == GainMent.LOAD,
            enter = fadeIn() + scaleIn() + expandVertically(),
            exit = fadeOut() + scaleOut() + shrinkVertically(),
        ) {
            val infinite = rememberInfiniteTransition()
            val rotating by infinite.animateFloat(
                0F, 360F, infiniteRepeatable(tween(500, easing = LinearEasing))
            )
            Column(
                modifier = Modifier.padding(bottom = 10.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconView(
                    painterResource(Res.drawable.iconrest), Modifier.size(25.dp).graphicsLayer {
                        rotationZ = rotating
                    })
                TextView(
                    modifier = Modifier.height(25.dp), fonttext = gainlist.gainment.text
                )
            }
        }
        if (gainlist.gainment == GainMent.OFFS || gainlist.gainment == GainMent.NONE || gainlist.gainment == GainMent.FAIL) {
            TextView(
                modifier = Modifier.height(25.dp), fonttext = gainlist.gainment.text
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            gainlist.gaindata.also { listdata ->
                if (listdata.isNotEmpty()) {
                    listdata.fastForEachIndexed { loop, item ->
                        item(loop) {
                            val isinhave by remember(item, appsdata.cytqlist) {
                                derivedStateOf {
                                    appsdata.cytqlist.any { (it.cytqcity.location.lat == item.location.lat && it.cytqcity.location.lng == item.location.lng) || it.cytqcity.name == item.name }
                                }
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 15.dp).sizedunt {
                                    onscopes.launch {
                                        if (isinhave) {
                                            warnview.show("当前城市已经添加")
                                        } else {
                                            cansadds {
                                                addscity(item)
                                            }
                                        }
                                    }
                                }.background(
                                    if (isinhave) {
                                        FORM.CARD
                                    } else {
                                        FORM.MAIN
                                    }, FORM.QJYJ
                                ).fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
                                    15.dp, Alignment.Start
                                )
                            ) {
                                TextView(fonttext = item.name.substringAfterLast(' '))
                                TextView(
                                    fonttext = item.name.substringBeforeLast(' ', ""), fontskin = FORM.GRAY
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}