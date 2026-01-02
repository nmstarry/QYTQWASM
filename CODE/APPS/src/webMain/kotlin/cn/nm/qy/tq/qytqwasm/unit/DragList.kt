package cn.nm.qy.tq.qytqwasm.unit

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import cn.nm.qy.tq.qytqwasm.tool.pointers
import kotlinx.coroutines.launch

@Composable
fun LazyItemScope.DragItem(
    modifier: Modifier = Modifier,
    modifies: Modifier = Modifier,
    dragment: DragMemt,
    itemloop: Int,
    contents: @Composable (Modifier, Modifier) -> Unit
) {
    val onscopes = rememberCoroutineScope()
    val slipview = remember { Animatable(0F) }
    var dragging by remember { mutableStateOf(false) }

    val modified by remember(dragging) {
        derivedStateOf {
            if (dragging) {
                Modifier
                    .graphicsLayer {
                        translationY = slipview.value
                    }
                    .zIndex(1F)
            } else {
                Modifier
                    .animateItem()
                    .zIndex(0F)
            }
        }
    }
    contents(
        modifier.then(modified), modifies.pointers(
            keysmark = itemloop,
            initiate = {
                if (dragment.itemloop == null) {
                    dragment.itemloop = itemloop
                    dragging = true
                }
                dragment.initiate()
            },
            dragging = {
                if (dragging) {
                    onscopes.launch {
                        slipview.snapTo(slipview.value + it.y)
                    }
                    onscopes.launch {
                        dragment.dragmove(slipview.value + it.y)?.also { item ->
                            dragment.itemsize?.also { itemsize ->
                                slipview.snapTo(slipview.value + itemsize)
                            }
                            dragment.trimlist(item.moveitem, item.realitem)
                            /*禁止滚动*/
                            if (
                                item.moveitem == dragment.listment.firstVisibleItemIndex ||
                                item.realitem == dragment.listment.firstVisibleItemIndex
                            ) {
                                dragment.listment.requestScrollToItem(
                                    dragment.listment.firstVisibleItemIndex,
                                    dragment.listment.firstVisibleItemScrollOffset,
                                )
                            }
                        }
                    }
                }
            },
            finishes = {
                onscopes.launch {
                    slipview.animateTo(0F)
                    dragging = false
                    dragment.finishes()
                }
                if (dragment.itemloop != null) {
                    dragment.itemloop = null
                    dragment.itemsize = null
                }
            }
        )
    )
}

class DragMemt(val listment: LazyListState, val trimlist: (Int, Int) -> Unit) {

    var itemsize by mutableStateOf<Int?>(null)
    var itemloop by mutableStateOf<Int?>(null)
    val viewlist get() = listment.layoutInfo.visibleItemsInfo
    fun initiate() {/*按下*/
    }

    fun dragmove(itemmove: Float): TrimItem? {/*拖动*/
        itemloop?.also { loopitem ->
            val realitem = viewlist.firstOrNull { item -> item.index == loopitem }
            val realsize = realitem?.size ?: 0
            val realmove = realitem?.offset ?: 0
            when {
                itemmove < 0 -> { /*向上*/
                    viewlist.firstOrNull { item -> item.index == loopitem - 1 }
                        ?.also {
                            val holdfate =
                                (it.size / 2 + realsize / 2) + (realmove - it.offset - it.size)
                            if (itemmove < -holdfate) {
                                itemsize = realmove - it.offset
                                itemloop = it.index
                                /*trimlist(it.index, loopitem)*/
                                return TrimItem(it.index, loopitem)
                            }
                        }
                }

                itemmove > 0 -> {/*向下*/
                    viewlist.firstOrNull { item -> item.index == loopitem + 1 }
                        ?.also {
                            val holdfate =
                                (it.size / 2 + realsize / 2) + (it.offset - realmove - realsize)
                            if (itemmove > holdfate) {
                                itemsize = -(it.offset - realmove) - (it.size - realsize)
                                itemloop = it.index
                                /*trimlist(it.index, loopitem)*/
                                return TrimItem(it.index, loopitem)
                            }
                        }
                }
            }
        }
        return null
    }

    fun finishes() {/*抬起*/
    }
}

data class TrimItem(var realitem: Int, var moveitem: Int)