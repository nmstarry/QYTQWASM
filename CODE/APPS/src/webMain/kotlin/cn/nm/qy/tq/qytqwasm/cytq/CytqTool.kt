package cn.nm.qy.tq.qytqwasm.cytq

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import qytqwasm.apps.generated.resources.*
import kotlin.math.roundToInt

enum class CytqMode(
    val skin: Color,
    val icon: DrawableResource,
    val code: String,
    val text: String,
) {
    CLEAR_DAY(Color(0XFF60A0E0), Res.drawable.qyqr, "CLEAR_DAY", "晴天"),
    CLEAR_NIGHT(Color(0XFF60A0E0), Res.drawable.qyqy, "CLEAR_NIGHT", "晴天"),
    PARTLY_CLOUDY_DAY(Color(0XFF60A0E0), Res.drawable.qyyr, "PARTLY_CLOUDY_DAY", "多云"),
    PARTLY_CLOUDY_NIGHT(Color(0XFF60A0E0), Res.drawable.qyyy, "PARTLY_CLOUDY_NIGHT", "多云"),
    CLOUDY(Color(0XFF80A0C0), Res.drawable.qyyt, "CLOUDY", "阴天"),
    LIGHT_HAZE(Color(0XFF80A0A0), Res.drawable.qywm, "LIGHT_HAZE", "轻度雾霾"),
    MODERATE_HAZE(Color(0XFF80A0A0), Res.drawable.qywm, "MODERATE_HAZE", "中度雾霾"),
    HEAVY_HAZE(Color(0XFF80A0A0), Res.drawable.qywm, "HEAVY_HAZE", "重度雾霾"),
    LIGHT_RAIN(Color(0XFF60A0E0), Res.drawable.qyxy, "LIGHT_RAIN", "小雨"),
    MODERATE_RAIN(Color(0XFF60A0E0), Res.drawable.qyzy, "MODERATE_RAIN", "中雨"),
    HEAVY_RAIN(Color(0XFF60A0E0), Res.drawable.qydy, "HEAVY_RAIN", "大雨"),
    STORM_RAIN(Color(0XFF60A0E0), Res.drawable.qyby, "STORM_RAIN", "暴雨"),
    FOG(Color(0XFF80A0A0), Res.drawable.qyyw, "FOG", "大雾"),
    LIGHT_SNOW(Color(0XFF60A0E0), Res.drawable.qyxx, "LIGHT_SNOW", "小雪"),
    MODERATE_SNOW(Color(0XFF60A0E0), Res.drawable.qyzx, "MODERATE_SNOW", "中雪"),
    HEAVY_SNOW(Color(0XFF60A0E0), Res.drawable.qydx, "HEAVY_SNOW", "大雪"),
    STORM_SNOW(Color(0XFF60A0E0), Res.drawable.qybx, "STORM_SNOW", "暴雪"),
    DUST(Color(0XFFE0A060), Res.drawable.qyfc, "DUST", "浮尘"),
    SAND(Color(0XFFE0A060), Res.drawable.qyys, "SAND", "沙尘"),
    WIND(Color(0XFF80A0C0), Res.drawable.qydf, "WIND", "大风"),
    NULL(Color(0XFF000000), Res.drawable.qywz, "NULL", "未知");

    companion object {
        fun gainmode(code: String?): CytqMode {
            return when (code) {
                "CLEAR_DAY" -> CLEAR_DAY
                "CLEAR_NIGHT" -> CLEAR_NIGHT
                "PARTLY_CLOUDY_DAY" -> PARTLY_CLOUDY_DAY
                "PARTLY_CLOUDY_NIGHT" -> PARTLY_CLOUDY_NIGHT
                "CLOUDY" -> CLOUDY
                "LIGHT_HAZE" -> LIGHT_HAZE
                "MODERATE_HAZE" -> MODERATE_HAZE
                "HEAVY_HAZE" -> HEAVY_HAZE
                "LIGHT_RAIN" -> LIGHT_RAIN
                "MODERATE_RAIN" -> MODERATE_RAIN
                "HEAVY_RAIN" -> HEAVY_RAIN
                "STORM_RAIN" -> STORM_RAIN
                "FOG" -> FOG
                "LIGHT_SNOW" -> LIGHT_SNOW
                "MODERATE_SNOW" -> MODERATE_SNOW
                "HEAVY_SNOW" -> HEAVY_SNOW
                "STORM_SNOW" -> STORM_SNOW
                "DUST" -> DUST
                "SAND" -> SAND
                "WIND" -> WIND
                else -> NULL
            }
        }
    }
}

enum class CytqWind(
    val rank: Int,/*等级*/
    val fate: ClosedFloatingPointRange<Float>,/*米每秒的*/
    val norm: IntRange,/*千米每秒*/
    val text: String,/*名称*/
    val look: String,/*文字*/
) {
    LEVELS00(0, 0.0F..0.2F, 0..1, "无风", "炊烟直上"),
    LEVELS01(1, 0.3F..1.5F, 1..5, "软风", "烟示风向"),
    LEVELS02(2, 1.6F..3.3F, 6..11, "轻风", "感觉有风"),
    LEVELS03(3, 3.4F..5.4F, 12..19, "微风", "旌旗展开"),
    LEVELS04(4, 5.5F..7.9F, 20..28, "和风", "吹起尘土"),
    LEVELS05(5, 8.0F..10.7F, 29..38, "清风", "小树摇摆"),
    LEVELS06(6, 10.8F..13.8F, 39..49, "强风", "电线有声"),
    LEVELS07(7, 13.9F..17.1F, 50..61, "劲风（疾风）", "步行困难"),
    LEVELS08(8, 17.2F..20.7F, 62..74, "大风", "折毁树枝"),
    LEVELS09(9, 20.8F..24.4F, 75..88, "烈风", "小损房屋"),
    LEVELS10(10, 24.5F..28.4F, 89..102, "狂风", "拔起树木"),
    LEVELS11(11, 28.5F..32.6F, 103..117, "暴风", "损毁重大"),
    LEVELS12(12, 32.7F..36.9F, 117..134, "台风（一级飓风）", "摧毁极大"),
    LEVELS13(13, 37.0F..41.4F, 134..149, "台风（一级飓风）", "摧毁极大"),
    LEVELS14(14, 41.5F..46.1F, 150..166, "加强台风（二级飓风）", "摧毁极大"),
    LEVELS15(15, 46.2F..50.9F, 167..183, "加强台风（三级飓风）", "摧毁极大"),
    LEVELS16(16, 51.0F..56.0F, 184..201, "超强台风（三级飓风）", "摧毁极大"),
    LEVELS17(17, 56.1F..61.2F, 202..220, "超强台风（四级飓风）", "摧毁极大"),
    LEVELS18(18, 61.3F..Float.POSITIVE_INFINITY, 221..250, "	超强台风（四级飓风）", "摧毁极大"),
    LEVELS19(
        19,
        Float.POSITIVE_INFINITY..Float.POSITIVE_INFINITY,
        250..999,
        "   超级台风（五级飓风）",
        "摧毁极大"
    );

    companion object {
        fun windturn(data: Float): String {
            return listOf(
                "正北", "正北偏东", "东北", "正东偏北",
                "正东", "正东偏南", "东南", "正南偏东",
                "正南", "正南偏西", "西南", "正西偏南",
                "正西", "正西偏北", "西北", "正北偏西"
            )[((((data % 360) + 360) % 360 + 11.25) / 22.5).toInt() % 16]
        }

        fun windrank(data: Float): CytqWind {
            return entries.firstOrNull { ((data * 10).roundToInt() / 10) in it.norm }
                ?: entries.first()
        }
    }
}

enum class CytqAqis(
    val l0: Int,
    val l1: Int,
    val l2: Int,
    val l3: Int,
    val l4: Int,
    val l5: Int,
    val l6: Int
) {
    IAQI(0, 50, 100, 150, 200, 300, /*400,*/500)
}

enum class AqisRank(
    val text: String,
    val long: String,
    val skip: Color,
    val rank: Int,
) {
    A1("优质", "优质", Color(0XFF80C000), 1),
    A2("良好", "良好", Color(0XFFFFC000), 2),
    A3("轻度", "轻度污染", Color(0XFFFF8000), 3),
    A4("中度", "中度污染", Color(0XFFFF4040), 4),
    A5("重度", "重度污染", Color(0XFFC00000), 5),
    A6("严重", "严重污染", Color(0XFF800000), 6),
    NO("暂无", "暂无数据", Color(0XFFC0C0C0), 0);

    companion object {
        fun airsrank(data: Int?, type: CytqAqis): AqisRank {
            return when (data) {
                in type.l0..type.l1 -> A1
                in type.l1..type.l2 -> A2
                in type.l2..type.l3 -> A3
                in type.l3..type.l4 -> A4
                in type.l4..type.l5 -> A5
                in type.l5..type.l6 -> A6
                else -> NO
            }
        }
    }
}

data class CytqWarn(
    val rank: String,/*等级*/
    val form: Color,/*颜色*/
    val type: String,/*类型*/
) {
    companion object {
        fun warntype(data: String): CytqWarn {
            val warntyhpe = when (data.take(2)) {
                "01" -> "台风"
                "02" -> "暴雨"
                "03" -> "暴雪"
                "04" -> "寒潮"
                "05" -> "大风"
                "06" -> "沙尘暴"
                "07" -> "高温"
                "08" -> "干旱"
                "09" -> "雷电"
                "10" -> "冰雹"
                "11" -> "霜冻"
                "12" -> "大雾"
                "13" -> "霾"
                "14" -> "道路结冰"
                "15" -> "森林火灾"
                "16" -> "雷雨大风"
                else -> "未知"
            }
            return when (data.takeLast(2)) {
                "01" -> CytqWarn("蓝色", Color(0XFF00A0FF), warntyhpe)
                "02" -> CytqWarn("黄色", Color(0XFFFFC000), warntyhpe)
                "03" -> CytqWarn("橙色", Color(0XFFFF8000), warntyhpe)
                "04" -> CytqWarn("红色", Color(0XFFFF4040), warntyhpe)
                else -> CytqWarn("未知", Color(0XFF404040), warntyhpe)
            }
        }
    }
}
