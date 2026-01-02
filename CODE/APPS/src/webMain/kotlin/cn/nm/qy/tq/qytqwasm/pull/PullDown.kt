package cn.nm.qy.tq.qytqwasm.pull

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScrollModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.requireDensity
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.nm.qy.tq.qytqwasm.cytq.AppsData
import cn.nm.qy.tq.qytqwasm.cytq.CytqData
import cn.nm.qy.tq.qytqwasm.mold.ViewMold
import cn.nm.qy.tq.qytqwasm.tool.HttpTool
import cn.nm.qy.tq.qytqwasm.unit.TextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.time.Clock

@Composable
fun PullDownFoldView(
    modifier: Modifier = Modifier,
    dragment: PullDownMoldData,
    pullment: PullDownFoldMent = rememberPullDownFoldMent(),
    callback: () -> Unit,
    pullview: @Composable ColumnScope.(PullDownFoldMent) -> Unit = {
        TextView(
            modifier = Modifier.fillMaxWidth().background(Color(0x20000000)).height(50.dp * it.realment),
            fonttext = dragment.actsment.text,
        )
    },
    contents: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier.pulldowndragacts(
                pullment = pullment, dragment = dragment.dragment, callback = callback
            ), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(pullment.realment) {
            if (!dragment.dragment) {
                when {
                    pullment.realment < 1 -> dragment.trimment(ActsMent.PULL)
                    pullment.realment > 1 -> dragment.trimment(ActsMent.FREE)
                }
            }
        }
        pullview(pullment)
        contents()
    }
}

@Composable
fun rememberPullDownMoldData(): PullDownMoldData {
    return viewModel { PullDownMoldData() }
}

enum class ActsMent(val text: String) {
    PULL("下拉刷新"), FREE("释放刷新"), LOAD("正在刷新"), OFFS("网络异常"), DONE("刷新成功"), FAIL("刷新失败"),
}

class PullDownMoldData : ViewModel() {

    /*行为状态*/
    var actsment by mutableStateOf(ActsMent.PULL)
        private set

    /*加载状态*/
    val loaddata by derivedStateOf { actsment == ActsMent.LOAD }

    /*拖拽状态*/
    val dragment by derivedStateOf { actsment != ActsMent.PULL && actsment != ActsMent.FREE }

    /*结束状态*/
    val finishes by derivedStateOf {
        actsment == ActsMent.DONE || actsment == ActsMent.FAIL || actsment == ActsMent.OFFS
    }

    /*延时任务*/
    private var lagsjobs: Job? = null

    fun trimment(trimment: ActsMent) {
        lagsjobs?.cancel()
        actsment = trimment
        if (finishes) {
            lagsjobs = viewModelScope.launch {
                delay(1000)
                if (finishes) {
                    actsment = ActsMent.PULL
                }
            }
        }
    }

    fun requests(realpage: CytqData, viewmold: ViewMold, appsdata: AppsData) {
        if (dragment) {
            return
        }
        viewModelScope.launch {
            trimment(ActsMent.LOAD)
            HttpTool.gainowns(
                realpage.cytqcity.location.lng,
                realpage.cytqcity.location.lat,
            ) { gain ->
                if (gain.gainment == true && gain.gaindata != null) {
                    val gaindata = realpage.copy(cytqowns = gain.gaindata, maketime = Clock.System.now().toEpochMilliseconds())
                    viewmold.savedata(
                        appsdata.copy(
                        cytqlist = appsdata.cytqlist.map {
                            if (it.maketime == realpage.maketime) {
                                gaindata
                            } else {
                                it
                            }
                        }))
                    trimment(ActsMent.DONE)
                } else {
                    trimment(ActsMent.FAIL)
                }
            }
        }

    }


    override fun onCleared() {
        lagsjobs?.cancel()
        super.onCleared()
    }
}

@Composable
fun rememberPullDownFoldMent(): PullDownFoldMent {
    return rememberSaveable(saver = PullDownFoldMent.savedata) { PullDownFoldMent() }
}

/*该动画运行时会打断列表及其子列表滚动行为*/
class PullDownFoldMent(val foldment: Animatable<Float, AnimationVector1D>) {

    constructor(animated: Float = 0f) : this(Animatable(animated, Float.VectorConverter))

    val realment get() = foldment.value

    val isinruns get() = foldment.isRunning

    suspend fun loadment() {
        foldment.animateTo(1F)
    }

    suspend fun stopment() {
        foldment.animateTo(0F)
    }

    suspend fun leapment(data: Float) {
        foldment.snapTo(data)
    }

    companion object {
        val savedata = Saver<PullDownFoldMent, Float>(
            save = { it.foldment.value },
            restore = { PullDownFoldMent(it) },
        )
    }
}

fun Modifier.pulldowndragacts(
    dragment: Boolean,
    pullment: PullDownFoldMent,
    enabling: Boolean = true,
    callback: () -> Unit,
): Modifier = this then PullDownFoldActs(
    dragment,
    pullment,
    enabling,
    callback,
)

data class PullDownFoldActs(
    val dragment: Boolean,
    val pullment: PullDownFoldMent,
    val enabling: Boolean,
    val callback: () -> Unit,
) : ModifierNodeElement<PullDownFoldNode>() {
    override fun create() = PullDownFoldNode(
        dragment,
        pullment,
        callback,
        enabling,
    )

    override fun update(node: PullDownFoldNode) {
        node.callback = callback
        node.enabling = enabling
        node.pullment = pullment
        if (node.dragment != dragment) {
            node.dragment = dragment
            node.trimdate()
        }
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "PullDownFoldNode"
        properties["dragment"] = dragment
        properties["pullment"] = pullment
        properties["enabling"] = enabling
        properties["callback"] = callback
    }
}

class PullDownFoldNode(
    var dragment: Boolean,
    var pullment: PullDownFoldMent,
    var callback: () -> Unit,
    var enabling: Boolean,
) : DelegatingNode(), NestedScrollConnection {

    override val shouldAutoInvalidate: Boolean get() = false

    private val barriers = 50.dp

    private val multiple = 0.5F

    /*布局实际移动距离*/
    private var movegaps by mutableFloatStateOf(0F)

    /*手指实际下拉距离*/
    private var pullgaps by mutableFloatStateOf(0F)

    /*修改后的下拉距离*/
    private val trimgaps
        get() = pullgaps * multiple

    /*下拉进度*/
    private val progress
        get() = trimgaps / tripgaps

    /*刷新回调触发距离*/
    private val tripgaps
        get() = with(requireDensity()) { barriers.roundToPx() }

    private var nestroll = nestedScrollModifierNode(this, null)

    /*节点附加*/
    override fun onAttach() {
        delegate(nestroll)
        coroutineScope.launch {
            pullment.leapment(if (dragment) 1F else 0F)
        }
        movegaps = if (dragment) tripgaps.toFloat() else 0F
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        return when {
            pullment.isinruns || !enabling -> Offset.Zero
            source == NestedScrollSource.UserInput && available.y < 0 -> dragacts(available)
            else -> Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        return when {
            pullment.isinruns || !enabling -> Offset.Zero
            source == NestedScrollSource.UserInput -> {
                val availably = dragacts(available)
                coroutineScope.launch {
                    if (!pullment.isinruns) {
                        pullment.leapment(movegaps / tripgaps)
                    }
                }
                availably
            }

            else -> Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        return Velocity(0F, releases(available.y))
    }

    /*触发调用刷新回调事件*/
    private suspend fun releases(velocity: Float): Float {/*加载状态禁止消耗*/
        if (dragment) {
            return 0F
        }/*触发刷新方法回调*/
        if (trimgaps > tripgaps) {
            callback()
        }
        val consumed = when {
            pullgaps == 0F -> 0F/*没有拖动没有消耗*/
            velocity <= 0F -> 0F/*向上滑动正常滚动*/
            else -> velocity/*向下滑动消费所有*/
        }
        if (pullgaps != 0F && movegaps != 0F) {
            stopment() /*没有触发无需执行恢复*/
        }
        return consumed
    }

    /*嵌套滚动连接辅助方法 */
    private fun dragacts(availably: Offset): Offset {
        return Offset(
            0F, if (dragment) {
                0F
            } else {
                val realmove = (pullgaps + availably.y).coerceAtLeast(0F)  /*计算实时拖动偏移*/
                val consumes = realmove - pullgaps /*计算消耗拖动距离*/
                pullgaps = realmove /*更新下拉距离数据*/
                movegaps = if (trimgaps <= tripgaps) {
                    trimgaps /*如果拖动没有触发刷新位置就是调整后的距离*/
                } else {
                    val tensions = (abs(progress) - 1.0F).coerceIn(0F, 2F)/*计算超出触发回调的百分比并限制在两倍之内*/
                    tripgaps + tripgaps * (tensions - tensions.pow(2) / 4)/*计算非线性张力并增加下拉距离的拖动难度值*/
                } /*计算垂直偏移量*/
                consumes
            }
        )
    }

    fun trimdate() {
        coroutineScope.launch {
            if (!dragment) {
                stopment()
            } else {
                loadment()
            }
        }
    }

    private suspend fun loadment() {
        try {
            pullment.loadment()
        } finally {
            if (isAttached) {
                pullgaps = tripgaps.toFloat()
                movegaps = tripgaps.toFloat()
            }
        }
    }

    private suspend fun stopment() {
        try {
            pullment.stopment()
        } finally {
            pullgaps = 0F
            movegaps = 0F
        }
    }
}