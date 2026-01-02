package cn.nm.qy.tq.qytqwasm.unit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.util.fastForEach

/*重叠布局*/
@Composable
fun HeapTier(
    modifier: Modifier = Modifier, contents: @Composable () -> Unit = {}
) {
    Layout(
        modifier = modifier, content = contents
    ) { measurable, constraint ->
        val item = measurable.map { it.measure(constraint) }
        val wide = constraint.maxWidth
        val high = constraint.maxHeight
        layout(wide, high) { item.fastForEach { it.place(0, 0) } }
    }
}

/*单层布局*/
@Composable
fun VoidTier(modifier: Modifier = Modifier) {
    Layout(
        modifier = modifier
    ) { measurable, constraint ->
        layout(constraint.minWidth, constraint.minHeight) {}
    }
}

/*移除父限制子布局*/
@Composable
fun FreeTier(
    modifier: Modifier = Modifier, contents: @Composable () -> Unit = {}
) {
    Layout(
        modifier = modifier, content = contents
    ) { measurable, constraint ->
        val item = measurable.map { it.measure(constraint) }
        val wide = constraint.minWidth
        val high = constraint.minHeight
        layout(wide, high) { item.fastForEach { it.place(0, 0) } }
    }
}

