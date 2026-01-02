package cn.nm.qy.tq.qytqwasm.cytq

import kotlinx.serialization.Serializable

/*总体数据*/
@Serializable
data class AppsData(
    var accepted: Boolean? = null,
    var cytqlist: List<CytqData> = listOf(),
)

@Serializable
data class CytqData(
    var serially:/*排序位置*/ Int,
    var maketime:/*创建时间*/ Long,
    var cytqcity: CytqCity.Places,
    var cytqowns: CytqOwns,
)

/*获取城市*/

@Serializable
data class GainCity(
    var gainment: GainMent = GainMent.STOP,
    var gaindata: List<CytqCity.Places> = listOf()
)

/*获取数据*/

@Serializable
data class GainOwns(
    val gainment: Boolean? = null,
    val gaindata: CytqOwns? = null,
)


@Serializable
enum class GainMent(val text: String) {
    LOAD("正在搜索"),
    STOP("停止搜索"),
    DONE("搜索完成"),
    FAIL("网络异常"),
    OFFS("网络异常搜索失败"),
    NONE("没有找到相关城市")
}
