package cn.nm.qy.tq.qytqwasm.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import cn.nm.qy.tq.qytqwasm.form.MainForm
import cn.nm.qy.tq.qytqwasm.page.IntoWays

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        MainForm {
            IntoWays(it)
        }
    }
}

@Composable
fun MainApps() {

}

