import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multimedia.platform)
    alias(libs.plugins.composes.multimedia.platform)
    alias(libs.plugins.kotlin.composes)
    alias(libs.plugins.kotlin.plugin.serializations)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class) wasmJs {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class) wasmJs {
        browser {
            commonWebpackConfig {
                devServer = devServer?.copy(
                    proxy = mutableListOf(
                        KotlinWebpackConfig.DevServer.Proxy(
                            context = mutableListOf("/xxxxx"),
                            target = "https://api.caiyunapp.com",
                            pathRewrite = mutableMapOf("^/xxxxx" to ""),
                            changeOrigin = true,
                            secure = false
                        )
                    )
                )
            }
            binaries.executable()
        }
    }/*生产环境请移除掉并修正请求函数*/
    //生产环境静态文件目录：D:\NM\QYTQWASM\APPS\build\dist\wasmJs\productionExecutable


    compilerOptions {
        freeCompilerArgs.add("-Xexplicit-backing-fields")
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.compose.ui:ui:1.10.0-rc02")
            implementation("org.jetbrains.compose.runtime:runtime:1.10.0-rc02")
            implementation("org.jetbrains.compose.animation:animation:1.10.0-rc02")
            implementation("org.jetbrains.compose.foundation:foundation:1.10.0-rc02")

            implementation("org.jetbrains.compose.material3:material3:1.9.0")
            implementation("org.jetbrains.compose.material3:material3-adaptive-navigation-suite:1.9.0")

            implementation("org.jetbrains.compose.components:components-resources:1.10.0-rc02")
            implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0-rc02")

            implementation(libs.navigation.browsers)/*导航界面*/


            implementation(libs.navigation.ui)/*导航界面*/
            implementation(libs.material.adaptive.navigation)/*适应屏幕*/
            implementation(libs.lifecycles.viewmodels.navigation)/*视图模型*/

            implementation(libs.androidx.lifecycles.viewmodels.composes)
            implementation(libs.androidx.lifecycles.runstime.composes)

            implementation(project.dependencies.platform(libs.ktor.billofmaterial))/*版本控制*/
            implementation(libs.ktor.client.core)/*核心依赖*/
            implementation(libs.ktor.client.content.negotiation)/*内容协商*/
            implementation(libs.ktor.serialization.kotlinx.json)/*序列化的*/
            implementation(libs.ktor.client.cbio)/*引擎依赖*/
            implementation(libs.ktor.client.logs)/*日志记录*/

            implementation(libs.kotlin.serializations.json)/*序列化库插件*/
            implementation(libs.kotlin.serializations.core)/*序列化库核心*/

            //implementation(libs.androidx.datastor.preference.core)/*数据存储*/
            //implementation(libs.androidx.datastor.core)/*数据存储*/

            implementation("org.jetbrains.kotlinx:kotlinx-browser:0.5.0")/*互操作库*/
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")/*时间日期*/
        }
    }

}


