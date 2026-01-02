package cn.nm.qy.tq.qytqwasm.main

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import cn.nm.qy.tq.qytqwasm.form.FORM
import cn.nm.qy.tq.qytqwasm.unit.TextView
import kotlinx.coroutines.delay

@Composable
fun WarnView(
    show: Boolean, text: String, time: Long, mask: Boolean, shut: () -> Unit
) {
    var displays by rememberSaveable { mutableStateOf(show) }
    LaunchedEffect(show, time, text) {
        if (show) {
            delay(time)
            shut()
        }
    }
    LaunchedEffect(show) {
        if (!show) {
            delay(100)
        }
        displays = show
    }
    val positionprovider = remember {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect, windowSize: IntSize, layoutDirection: LayoutDirection, popupContentSize: IntSize
            ): IntOffset {
                return windowSize.center - popupContentSize.center
            }
        }
    }
    if (show || displays) {
        Popup(popupPositionProvider = positionprovider) {
            Box(
                modifier = Modifier.then(
                    if (mask) {
                        Modifier.fillMaxSize()
                    } else {
                        Modifier
                    }
                ).systemBarsPadding(), contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = show && displays, enter = fadeIn() + scaleIn(tween(100), initialScale = 0.8f), exit = fadeOut() + scaleOut(tween(100), targetScale = 0.8f)
                ) {
                    TextView(
                        fonttext = text,
                        fontskin = FORM.PURE,
                        aligning = TextAlign.Center,
                        mostline = 10,
                        modifier = Modifier.background(FORM.GRAY, FORM.QJYJ).padding(15.dp)
                    )
                }
            }
        }
    }
}

data class WarnMent(
    val text: String, val time: Long, val mask: Boolean
)

@Composable
fun rememberWarnMent(): WarnImpl {
    val warnment = rememberSaveable(saver = WarnImpl.warnsave) { WarnImpl() }

    warnment.warnment.also {
        WarnView(
            show = warnment.cansshow, text = it.text, time = it.time, mask = it.mask
        ) {
            warnment.hide()
        }
    }
    return warnment
}

class WarnImpl {
    var cansshow by mutableStateOf(false)

    var warnment by mutableStateOf(WarnMent("", 0L, true))
        private set

    fun show(text: String, time: Long = 1500, mask: Boolean = false) {
        warnment = WarnMent(text, time, mask)
        cansshow = true
    }

    fun hide() {
        cansshow = false
    }

    companion object {
        val warnsave = listSaver(
            { warnImpl ->
                listOf(
                    warnImpl.cansshow,
                    warnImpl.warnment.text,
                    warnImpl.warnment.time,
                    warnImpl.warnment.mask
                )
            },
            { list ->
                val warnimpl = WarnImpl()
                warnimpl.cansshow = list[0] as Boolean
                warnimpl.warnment = WarnMent(
                    text = list[1] as String,
                    time = list[2] as Long,
                    mask = list[3] as Boolean
                )
                warnimpl
            }
        )
    }
}

