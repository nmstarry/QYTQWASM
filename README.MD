# 基于 Compose For Web Kotlin/Wasm 的天气预报应用网页端

#### [开发语言：Kotlin](https://kotlinlang.org/)

#### [界面框架：Compose Multiplatform](https://www.jetbrains.com.cn/compose-multiplatform/)

#### [网络请求：Ktor](https://ktor.io/)

#### [构建工具：Gradle](https://plugins.gradle.org/)

## 本项目采用了 MVVM 模式进行设计

<img src="FILE/JG.png" width="50%" alt=""/>
<img src="FILE/LC.png" width="50%" alt=""/>

## [访问 Kotlin / Js 版本](https://tojs.nmstarry.qzz.io/)

## [访问 Kotlin / Wasm 版本](https://qytq.nmstarry.qzz.io/)

## <span style="color: green;">Android 端演示视频</span>

https://github.com/user-attachments/assets/43838340-7835-40b1-9db5-1b1b07ff6789

## <span style="color: green;"> Web 端演示视频 </span>

https://github.com/user-attachments/assets/359c6542-825b-4f74-a8d8-2e0acbc89bf7

## <span style="color: red;">如果浏览器不支持 WASM 则会出现以下情况：</span>

1.显示以下图片内容，例如一加手机和魅族手机系统浏览器

####

<img src="FILE/BC.png" width="50%" alt=""/>

2.无法显示任何内容，网页名称和图标正常加载，控制台显示有关于 WASM 的异常（也可能不显示）

####

<img src="FILE/C1.png" width="50%" alt=""/>
<img src="FILE/C0.png" width="50%" alt=""/>
<img src="FILE/H0.png" width="50%" alt=""/>
<img src="FILE/H1.png" width="50%" alt=""/>

## 来自 Kotlin 官网的性能对比图

<img src="FILE/XN.png" width="50%" alt=""/>

## 移动端（Web）Kotlin/Js 和 Kotlin/Wasm

核心都是 WebAssembly ,区别就是 Kotlin/Wasm 比 Kotlin/Js 兼容性更差

<div style="display: flex; justify-content: space-around;">
  <img src="FILE/W1.png" width="16%" alt=""/>
  <img src="FILE/W2.png" width="16%" alt=""/>
  <img src="FILE/W3.png" width="16%" alt=""/>
  <img src="FILE/W4.png" width="16%" alt=""/>
  <img src="FILE/W0.png" width="16%" alt=""/>
</div>

## 移动端（Android）

<div style="display: flex; justify-content: space-around;">
  <img src="FILE/S1.png" width="20%" alt=""/>
  <img src="FILE/S2.png" width="20%" alt=""/>
  <img src="FILE/S3.png" width="20%" alt=""/>
  <img src="FILE/S0.png" width="20%" alt=""/>
</div>

## <span style="color: red;">已知问题</span>

1.嵌套滚动问题，代码来着 Android 端迁移，手机浏览器可以通过触摸横向滚动，桌面浏览器需要通过 Shift 键 + 鼠标滚轮滚动

2.中文乱码问题，Kotlin/Wasm 本身支持中、日、韩等非ASCII字符，根源在于 WebAssembly (Wasm) 底层没有原生的字符串类型，只能通过引入字体文件显示，字体文件未加载完成，中文字符会显示为乱码

3.无法返回问题，由于没有适配桌面端，“城市搜索页面”请点击浏览器导航按钮返回，或者点击空白处后按 Esc 键返回（类似 Android 焦点问题，所以要点击空白处，让输入框失去焦点，否则按 Esc 键无效，手机上访问直接通过系统返回手势即可），“已添加的城市页面” 请通过点击你所添加的城市进入天气数据查看页面（手机端操作相同）
