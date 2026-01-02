package cn.nm.qy.tq.qytqwasm.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.nm.qy.tq.qytqwasm.cytq.CytqCity
import cn.nm.qy.tq.qytqwasm.form.FORM
import cn.nm.qy.tq.qytqwasm.mold.LoadMold
import cn.nm.qy.tq.qytqwasm.mold.ViewMold
import cn.nm.qy.tq.qytqwasm.unit.IconView
import cn.nm.qy.tq.qytqwasm.unit.TextView
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import qytqwasm.apps.generated.resources.Res
import qytqwasm.apps.generated.resources.iconrest
import qytqwasm.apps.generated.resources.iconwarn
import qytqwasm.apps.generated.resources.selected

@Serializable
enum class LoadMent(val head: String, val text: String) {
    ACTSSTOP("", ""),
    DATALOAD("正在加载", "请稍等下"),
    DATAFAIL("添加失败", "请检查网络后重试"),
    DATADONE("添加成功", "请到城市列表查看"),
}

@Composable
fun LoadView(
    viewmold: ViewMold,
    loadsite: CytqCity.Places,
    shutview: () -> Unit,
) {
    val appsdata by viewmold.gaindata.collectAsState()

    val loadmold = viewModel { LoadMold() }
    DisposableEffect(Unit) {
        if (loadmold.loadment == LoadMent.ACTSSTOP) {
            loadmold.gainowns(viewmold, loadsite, appsdata)
        }
        onDispose {
            loadmold.loadment = LoadMent.ACTSSTOP
        }
    }

    LaunchedEffect(loadmold.loadment) {
        if (loadmold.loadment == LoadMent.DATADONE || loadmold.loadment == LoadMent.DATAFAIL) {
            delay(2000)
            shutview()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = loadmold.loadment,
            modifier = Modifier
                .background(FORM.PURE, FORM.QJYJ),
            transitionSpec = {
                (scaleIn(tween(100))) togetherWith (
                        scaleOut(tween(100)))
            },
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .background(FORM.PURE, FORM.QJYJ)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    15.dp,
                    Alignment.CenterVertically
                )
            ) {
                when (it) {
                    LoadMent.DATALOAD, LoadMent.DATAFAIL, LoadMent.DATADONE -> {
                        TextView(fonttext = it.head)
                        val infinite = rememberInfiniteTransition()
                        val rotating by infinite.animateFloat(
                            0F,
                            360F,
                            infiniteRepeatable(tween(500, easing = LinearEasing))
                        )
                        IconView(
                            painterResource(
                                when(it){
                                    LoadMent.DATALOAD -> Res.drawable.iconrest
                                    LoadMent.DATAFAIL -> Res.drawable.iconwarn
                                    LoadMent.DATADONE -> Res.drawable.selected
                                }
                            ),
                            Modifier
                                .size(25.dp)
                                .graphicsLayer {
                                    if (it == LoadMent.DATALOAD) {
                                        rotationZ = rotating
                                    }
                                }
                        )
                        TextView(
                            fonttext = it.text,
                            modifier = Modifier.padding(horizontal = 15.dp)
                        )
                    }

                    LoadMent.ACTSSTOP -> {}
                }
            }
        }
    }
}