package cn.nm.qy.tq.qytqwasm.page

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.NavDisplay.popTransitionSpec
import androidx.navigation3.ui.NavDisplay.predictivePopTransitionSpec
import androidx.navigation3.ui.NavDisplay.transitionSpec
import androidx.savedstate.serialization.SavedStateConfiguration
import cn.nm.qy.tq.qytqwasm.cytq.CytqCity
import cn.nm.qy.tq.qytqwasm.mold.ViewMold
import cn.nm.qy.tq.qytqwasm.tool.showlogs
import com.github.terrakok.navigation3.browser.HierarchicalBrowserNavigation
import com.github.terrakok.navigation3.browser.buildBrowserHistoryFragment
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/*页面表单*/
@Serializable
open class PAGE : NavKey {

    @Serializable
    object AVOW : PAGE()

    @Serializable
    data class HOME(val page: Int? = null) : PAGE()

    @Serializable
    object CITY : PAGE()

    @Serializable
    object FIND : PAGE()

    @Serializable
    object SETS : PAGE()

    @Serializable
    data class LOAD(val site: CytqCity.Places) : PAGE()
}

/*页面路由*/
@Composable
fun IntoWays(viewmold: ViewMold) {

    val appsdata by viewmold.gaindata.collectAsState()

    val matedata by remember {
        mutableStateOf(transitionSpec {
            slideInVertically(
                initialOffsetY = { it },
            ) togetherWith ExitTransition.KeepUntilTransitionsFinished
        } + popTransitionSpec {
            EnterTransition.None togetherWith slideOutVertically(
                targetOffsetY = { it },
            )
        } + predictivePopTransitionSpec {
            EnterTransition.None togetherWith slideOutVertically(
                targetOffsetY = { it },
            )
        })
    }

    val arranges = remember {
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(PAGE.AVOW::class, PAGE.AVOW.serializer())
                    subclass(PAGE.HOME::class, PAGE.HOME.serializer())
                    subclass(PAGE.CITY::class, PAGE.CITY.serializer())
                    subclass(PAGE.FIND::class, PAGE.FIND.serializer())
                    subclass(PAGE.SETS::class, PAGE.SETS.serializer())
                    subclass(PAGE.LOAD::class, PAGE.LOAD.serializer())
                }
            }
        }
    }

    appsdata.accepted?.also { accepted ->
        val backlist = rememberNavBackStack(
            arranges, if (!accepted) {
                PAGE.AVOW
            } else if (appsdata.cytqlist.isNotEmpty()) {
                PAGE.HOME()
            } else {
                PAGE.CITY
            }
        )

        HierarchicalBrowserNavigation {
            when (backlist.lastOrNull()) {
                is PAGE.AVOW -> buildBrowserHistoryFragment("AVOW")
                is PAGE.HOME -> buildBrowserHistoryFragment("HOME")
                is PAGE.CITY -> buildBrowserHistoryFragment("CITY")
                is PAGE.FIND -> buildBrowserHistoryFragment("FIND")
                is PAGE.SETS -> buildBrowserHistoryFragment("SETS")
                is PAGE.LOAD -> buildBrowserHistoryFragment("LOAD")
                else -> null
            }
        }

        LaunchedEffect(backlist) {
            showlogs(backlist)
        }

        val hintshow = remember { DialogSceneStrategy<NavKey>() }

        NavDisplay(
            modifier = Modifier.fillMaxSize(), backStack = backlist, sceneStrategy = hintshow, entryProvider = entryProvider {
                entry<PAGE.AVOW> {
                    PageAvow(viewmold, {
                        backlist.add(PAGE.CITY)
                        backlist.remove(PAGE.AVOW)
                    }) {
                        /*退出网页*/
                    }
                }
                entry<PAGE.HOME> {
                    PageHome(viewmold, it.page) {
                        backlist.add(PAGE.CITY)
                    }
                }
                entry<PAGE.CITY> {
                    PageCity(viewmold, {
                        backlist.add(PAGE.FIND)
                    }) { page ->
                        backlist.removeIf { it is PAGE.HOME }
                        backlist.add(PAGE.HOME(page))
                        backlist.remove(PAGE.CITY)
                    }
                }
                entry<PAGE.FIND>(metadata = matedata) {
                    PageFind(viewmold) {
                        if (!backlist.any { item -> item is PAGE.LOAD }) {
                            backlist.add(PAGE.LOAD(it))
                        }
                    }
                }
                entry<PAGE.LOAD>(
                    metadata = DialogSceneStrategy.dialog(DialogProperties(dismissOnClickOutside = false))
                ) {
                    LoadView(viewmold, it.site) {
                        backlist.remove(PAGE.LOAD(it.site))
                    }
                }
                entry<PAGE.SETS> {

                }
            }, transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it },
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it })
            }, popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it }) togetherWith slideOutHorizontally(
                    targetOffsetX = { it })
            }, predictivePopTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it }) togetherWith slideOutHorizontally(
                    targetOffsetX = { it })
            }, contentAlignment = Alignment.Center
        )
    }
}

fun <T : NavKey> NavBackStack<T>.removeIf(oncondit: (T?) -> Boolean) {
    onEach {
        if (oncondit(it)) {
            remove(it)
        }
    }
}