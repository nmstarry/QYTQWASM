package cn.nm.qy.tq.qytqwasm.page

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import cn.nm.qy.tq.qytqwasm.cytq.CytqData
import cn.nm.qy.tq.qytqwasm.cytq.CytqMode
import cn.nm.qy.tq.qytqwasm.form.FORM
import cn.nm.qy.tq.qytqwasm.main.rememberWarnMent
import cn.nm.qy.tq.qytqwasm.mold.ViewMold
import cn.nm.qy.tq.qytqwasm.tool.duntsets
import cn.nm.qy.tq.qytqwasm.tool.sizedunt
import cn.nm.qy.tq.qytqwasm.unit.DragItem
import cn.nm.qy.tq.qytqwasm.unit.DragMemt
import cn.nm.qy.tq.qytqwasm.unit.IconView
import cn.nm.qy.tq.qytqwasm.unit.TextView
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import qytqwasm.apps.generated.resources.Res
import qytqwasm.apps.generated.resources.iconmove
import qytqwasm.apps.generated.resources.selected
import qytqwasm.apps.generated.resources.selectee

@Composable
fun PageCity(
    viewmold: ViewMold,
    findcity: () -> Unit,
    backhome: (Int?) -> Unit
) {

    val appsdata by viewmold.gaindata.collectAsState()

    var isinedit by rememberSaveable { mutableStateOf(false) }

    var picklist by rememberSaveable { mutableStateOf(listOf<CytqData>()) }

    val warnview = rememberWarnMent()

    val onscopes = rememberCoroutineScope()

    LaunchedEffect(Unit){
        if (appsdata.cytqlist.isEmpty()){
            warnview.show("城市列表为空\n请先添加城市")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.Top)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextView(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .weight(1F),
                fonttext = "城市管理",
                fontsize = 20.sp,
            )
            TextView(
                fonttext = if (isinedit) {
                    "完成"
                } else {
                    "编辑"
                },
                fontsize = 20.sp,
                modifier = Modifier.sizedunt {
                    onscopes.launch {
                        viewmold.savedata(appsdata)
                        picklist = listOf()
                        isinedit = !isinedit
                    }
                }
            )
        }

        val listment = rememberLazyListState()

        val dragment = remember(listment) {
            DragMemt(listment) { real, move ->
                viewmold.trimdata { data ->
                    data.copy(
                        cytqlist = appsdata.cytqlist.sortedBy { it.serially }
                            .also { datalist ->
                                val serially = datalist[real].serially
                                datalist[real].serially = datalist[move].serially
                                datalist[move].serially = serially
                            }.sortedBy { it.serially }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1F)
                .clip(FORM.QJYJ),
            state = listment,
            verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            appsdata.cytqlist.sortedBy { it.serially }.fastForEachIndexed { loop, item ->
                item(item.maketime) {
                    DragItem(
                        dragment = dragment,
                        itemloop = loop
                    ) { modifier, modifies ->

                        val existing by remember { derivedStateOf { item in picklist } }

                        Box(
                            modifier = modifier
                                .duntsets(longdunt = {
                                    isinedit = true
                                    picklist = if (existing) {
                                        picklist - item
                                    } else {
                                        picklist + item
                                    }
                                }, onclicks = {
                                    if (isinedit) {
                                        picklist = if (existing) {
                                            picklist - item
                                        } else {
                                            picklist + item
                                        }
                                    } else {
                                        backhome(loop)
                                    }
                                })
                                .height(IntrinsicSize.Max)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            val realmode by remember(item.cytqowns.result.realtime) {
                                derivedStateOf {
                                    CytqMode.gainmode(item.cytqowns.result.realtime.skycon)
                                }
                            }

                            DrawBack(
                                modifier = Modifier.clip(FORM.QJYJ),
                                backskip = realmode.skin, loopvary = false
                            )

                            val editpads by animateDpAsState(
                                if (isinedit) {
                                    50.dp
                                } else {
                                    0.dp
                                }
                            )

                            Row(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxSize()
                                    .padding(horizontal = editpads),
                                horizontalArrangement = Arrangement.spacedBy(
                                    15.dp,
                                    Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1F),
                                    verticalArrangement = Arrangement.spacedBy(
                                        15.dp,
                                        Alignment.CenterVertically
                                    ),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    TextView(
                                        fonttext = "${item.cytqowns.result.realtime.temperature}℃ ",
                                        fontskin = FORM.PURE
                                    )
                                    TextView(fonttext = buildAnnotatedString {
                                        append(item.cytqcity.name.substringAfterLast(' '))
                                        withStyle(SpanStyle(color = FORM.TINT)) {
                                            append(
                                                " " + item.cytqcity.name.substringBeforeLast(
                                                    ' ',
                                                    ""
                                                )
                                            )
                                        }
                                    }, fontskin = FORM.PURE)
                                }

                                TextView(fonttext = realmode.text, fontskin = FORM.PURE)
                                Image(
                                    painterResource(realmode.icon),
                                    null,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            androidx.compose.animation.AnimatedVisibility(
                                isinedit,
                                modifier = Modifier.padding(15.dp),
                                enter = fadeIn(),
                                exit = fadeOut(),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    IconView(
                                        painterResource(Res.drawable.iconmove),
                                        modifies.size(20.dp),
                                        FORM.PURE
                                    )

                                    IconView(
                                        painterResource(
                                            if (existing) {
                                                Res.drawable.selected
                                            } else {
                                                Res.drawable.selectee
                                            }
                                        ),
                                        Modifier.size(20.dp),
                                        if (existing) {
                                            FORM.WARN
                                        } else {
                                            FORM.PURE
                                        }

                                    )
                                }
                            }


                        }
                    }


                }
            }
        }

        val backform by animateColorAsState(
            if (isinedit) {
                if (picklist.isEmpty()) {
                    FORM.CARD
                } else {
                    FORM.HEED
                }
            } else {
                if (appsdata.cytqlist.size <= 20) {
                    FORM.MAIN
                } else {
                    FORM.CARD
                }
            }
        )

        Box(
            modifier = Modifier
                .sizedunt {
                    if (isinedit) {
                        viewmold.savedata(
                            appsdata.copy(cytqlist = appsdata.cytqlist - picklist.toSet())
                        )

                        picklist = listOf()
                    } else {
                        if (appsdata.cytqlist.size <= 20) {
                            findcity()
                        } else {
                            warnview.show("最多只能添加二十个城市")
                        }
                    }
                }
                .padding(bottom = 15.dp)
                .fillMaxWidth()
                .height(50.dp)
                .background(backform, FORM.QJYJ),
            contentAlignment = Alignment.Center
        ) {
            TextView(
                fonttext = if (isinedit) {
                    "删除城市"
                } else {
                    "添加城市"
                }
            )
        }
    }
}
