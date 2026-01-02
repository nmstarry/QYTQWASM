package cn.nm.qy.tq.qytqwasm.cytq

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*城市数据*/

@Serializable
data class CytqCity(
    val status: String,/*状态*/
    val query: String,/*查询内容*/
    val places: List<Places>/*查询列表*/
) {

    @Serializable
    data class Places(
        @SerialName("formatted_address") val formattedAddress: String,
        val id: String, @SerialName("place_id")
        val placeId: String, val name: String,/*名称*/
        val location: Location/*经纬度*/
    ) {

        @Serializable
        data class Location(
            val lat: Double,/*纬度*/
            val lng: Double,/*经度*/
        )
    }
}

/*天气数据*/

@Serializable
data class CytqOwns(
    val status: String,/*状态*/
    @SerialName("api_version") val apiVersion: String,/*接口版本*/
    @SerialName("api_status") val apiStatus: String,/*接口状态*/
    val lang: String,/*语言*/
    val unit: String,/*单位*/
    val tzshift: Long,/*时区偏移秒数*/
    val timezone: String,/*时区*/
    @SerialName("server_time") val serverTime: Long,/*时间的戳*/
    val location: List<Double>,/*经度纬度*/
    val result: Result/*结果*/
) {/*天气*/

    @Serializable
    data class Result(
        val alert: Alert,/*预警*/
        val realtime: Realtime,/*实时*/
        val minutely: Minutely,/*分钟*/
        val hourly: Hourly,/*小时*/
        val daily: Daily,/*逐天*/
        val primary: Int,/*重要级别*/
        @SerialName("forecast_keypoint") val forecastKeypoint: String/*预测信息*/
    ) {/*预警*/

        @Serializable
        data class Alert(
            val status: String,/*状态*/
            val content: List<Content> /*内容*/
        ) {

            @Serializable
            data class Content(
                val province: String,/*省份*/
                val status: String,/*预警状态*/
                val code: String,/*预警代码*/
                val description: String,/*描述*/
                val regionId: String,/*地区代码*/
                val county: String,/*县城*/
                val pubtimestamp: Long,/*发布时间*/
                val latlon: List<Double>,/*经度纬度*/
                val city: String,/*城市*/
                val alertId: String,/*预警代码*/
                val title: String,/*标题*/
                val adcode: String,/*区域代码*/
                val source: String,/*发布单位*/
                val location: String,/*位置*/
                @SerialName("request_status") val requestStatus: String/*请求状态*/
            )
        }

        /*实时*/

        @Serializable
        data class Realtime(
            val status: String,/*状态*/
            val temperature: Float,/*地表温度*///❌
            val humidity: Float,/*湿度*///❌
            val cloudrate: Float,/*云量*/
            val skycon: String,/*天气现象*///❌
            val visibility: Float,/*地表水平能见度*///❌
            val dswrf: Float,/*向下短波辐射通量*/
            val wind: Wind,/*风的*///❌
            val pressure: Float,/*大气气压*/
            @SerialName("apparent_temperature") val apparentTemperature: Float,/*体感温度*///❌
            val precipitation: Precipitation,/*降水*///❌
            @SerialName("air_quality") val airQuality: AirQuality,/*空气质量*///❌
            @SerialName("life_index") val lifeIndex: LifeIndex/*生活指数*/
        ) {/*风的*/

            @Serializable
            data class Wind(
                val speed: Float,/*风速*/
                val direction: Float/*风向*/
            )


            @Serializable
            data class Precipitation(
                val local: Local,/*本地*/
                val nearest: Nearest? = null/*最近*/
            ) {

                @Serializable
                data class Local(
                    val status: String,/*状态*/
                    val datasource: String,/*数据来源*/
                    val intensity: Float/*降水强度*/
                )


                @Serializable
                data class Nearest(
                    val status: String,/*状态*/
                    val distance: Float,/*距离*/
                    val intensity: Float/*降水强度*/
                )
            }


            @Serializable
            data class AirQuality(
                val pm25: Int,/*PM25 浓度(μg/m3)*/
                val pm10: Int,/*PM10 浓度(μg/m3)*/
                val o3: Int,/*臭氧浓度(μg/m3)*/
                val so2: Int,/*二氧化氮浓度(μg/m3)*/
                val no2: Int,/*二氧化硫浓度(μg/m3)*/
                val co: Float,/*一氧化碳浓度(mg/m3)*/
                @SerialName("pm25_iaqi_chn") val pm25IaqiChn: Int,/*PM25 浓度(μg/m3)*///❌
                @SerialName("pm10_iaqi_chn") val pm10IaqiChn: Int,/*PM10 浓度(μg/m3)*///❌
                @SerialName("o3_iaqi_chn") val o3IaqiChn: Int,/*臭氧浓度(μg/m3)*///❌
                @SerialName("so2_iaqi_chn") val so2IaqiChn: Int,/*二氧化氮浓度(μg/m3)*///❌
                @SerialName("no2_iaqi_chn") val no2IaqiChn: Int,/*二氧化硫浓度(μg/m3)*///❌
                @SerialName("co_iaqi_chn") val coIaqiChn: Int,/*一氧化碳浓度(mg/m3)*///❌
                @SerialName("obs_time") val obsTime: Long,/*观测时间*///❌
                val aqi: Aqi,/*空气质量指数*///❌
                val description: AqiDescription/*空气质量说明*/
            ) {

                @Serializable
                data class Aqi(
                    val chn: Int,/*国标 AQI*/
                    val usa: Int/*美标 AQI*/
                )


                @Serializable
                data class AqiDescription(
                    val chn: String, val usa: String
                )
            }


            @Serializable
            data class LifeIndex(
                val ultraviolet: Value,/*紫外线*/
                val comfort: Value/*舒适度*/
            ) {

                @Serializable
                data class Value(
                    val index: Float, val desc: String
                )
            }
        }

        /*分钟*/

        @Serializable
        data class Minutely(
            val status: String,/*状态*/
            val datasource: String,/*数据来源*/
            @SerialName("precipitation_2h") val precipitation2h: List<Float>,/*两小时内降水*/
            val precipitation: List<Float>,/*降水*/
            val probability: List<Float>,/*降水概率*/
            val description: String/*说明*/
        )

        /*小时*/

        @Serializable
        data class Hourly(
            val status: String,/*状态*/
            val description: String,/*描述*/
            val precipitation: List<Precipitation>,/*降水*/
            val temperature: List<Temperature>,/*温度*///❌
            val wind: List<Wind>,/*风的*/
            val humidity: List<Humidity>,/*湿度*/
            val cloudrate: List<Cloudrate>,/*云率*/
            val skycon: List<Skycon>,/*天气现象*///❌
            val pressure: List<Pressure>,/*气压*/
            val visibility: List<Visibility>,/*可见距离*/
            val dswrf: List<Dswrf>,/*向下短波辐射通量*/
            @SerialName("air_quality") val airQuality: AirQuality/*空气质量*/
        ) {

            @Serializable
            data class Precipitation(
                val datetime: String, val value: Float
            )


            @Serializable
            data class Temperature(
                val datetime: String, val value: Float
            )


            @Serializable
            data class Wind(
                val datetime: String, val speed: Float,/*风速*/
                val direction: Float/*风向*/
            )


            @Serializable
            data class Humidity(
                val datetime: String, val value: Float
            )


            @Serializable
            data class Cloudrate(
                val datetime: String, val value: Float
            )


            @Serializable
            data class Skycon(
                val datetime: String, val value: String
            )


            @Serializable
            data class Pressure(
                val datetime: String, val value: Float
            )


            @Serializable
            data class Visibility(
                val datetime: String, val value: Float
            )


            @Serializable
            data class Dswrf(
                val datetime: String, val value: Float
            )


            @Serializable
            data class AirQuality(
                val aqi: List<Aqi>, val pm25: List<Pm25>
            ) {

                @Serializable
                data class Aqi(
                    val datetime: String, val value: Value
                ) {

                    @Serializable
                    data class Value(
                        val chn: Int, val usa: Int
                    )
                }


                @Serializable
                data class Pm25(
                    val datetime: String, val value: Int
                )
            }
        }

        /*逐天*/

        @Serializable
        data class Daily(
            val status: String,/*状态*/
            val astro: List<Astro>,/*天文*///❌
            val precipitation: List<Precipitation>,/*降水*/
            val temperature: List<Temperature>,/*温度*/
            val wind: List<Wind>,/*风的*/
            val humidity: List<Humidity>,/*湿度*/
            val cloudrate: List<Cloudrate>,/*云量*/
            val pressure: List<Pressure>,/*气压*/
            val visibility: List<Visibility>,/*地表水平的能见度*/
            val dswrf: List<Dswrf>,/*向下短波辐射通量*/
            @SerialName("air_quality") val airQuality: AirQuality,/*空气质量*///❌
            val skycon: List<Skycon>,/*全天主要天气现象*///❌
            @SerialName("skycon_08h_20h") val skycon08h20h: List<Skycon>,/*白天主要天气现象*///❌
            @SerialName("skycon_20h_32h") val skycon20h32h: List<Skycon>,/*夜晚主要天气现象*///❌
            @SerialName("life_index") val lifeIndex: LifeIndex/*生活指数*/
        ) {

            @Serializable
            data class Astro(
                val date: String,//❌
                val sunrise: Value,/*日出*///❌
                val sunset: Value,/*日落*///❌
            ) {

                @Serializable
                data class Value(
                    val time: String/*时间*///❌
                )
            }


            @Serializable
            data class Precipitation(
                val date: String,/*时间*/
                val max: Float,/*最大*/
                val min: Float,/*最小*/
                val avg: Float/*平均*/
            )


            @Serializable
            data class Temperature(
                val date: String, val max: Float, val min: Float, val avg: Float
            )


            @Serializable
            data class Wind(
                val date: String, val max: Value, val min: Value, val avg: Value
            ) {

                @Serializable
                data class Value(
                    val speed: Float,/*风速*/
                    val direction: Float/*风向*/
                )
            }


            @Serializable
            data class Humidity(
                val date: String, val max: Float, val min: Float, val avg: Float
            )


            @Serializable
            data class Cloudrate(
                val date: String, val max: Float, val min: Float, val avg: Float
            )


            @Serializable
            data class Pressure(
                val date: String, val max: Float, val min: Float, val avg: Float
            )


            @Serializable
            data class Visibility(
                val date: String, val max: Float, val min: Float, val avg: Float
            )


            @Serializable
            data class Dswrf(
                val date: String, val max: Float, val min: Float, val avg: Float
            )


            @Serializable
            data class AirQuality(
                val aqi: List<Aqi>,//❌
                val pm25: List<Pm25>
            ) {

                @Serializable
                data class Aqi(
                    val date: String, val max: Value, val avg: Value, val min: Value
                ) {

                    @Serializable
                    data class Value(
                        val chn: Int,/*国标*/
                        val usa: Int/*美标*/
                    )
                }


                @Serializable
                data class Pm25(
                    val date: String, val max: Int, val avg: Int, val min: Int
                )
            }


            @Serializable
            data class Skycon(
                val date: String, val value: String
            )


            @Serializable
            data class LifeIndex(
                val ultraviolet: List<Value>,/*紫外线*/
                val carWashing: List<Value>,/*洗车*/
                val dressing: List<Value>,/*穿衣*/
                val comfort: List<Value>,/*舒适度*/
                val coldRisk: List<Value>/*感冒*/
            ) {

                @Serializable
                data class Value(
                    val date: String,/*时间*/
                    val index: Int,/*等级*/
                    val desc: String/*描述*/
                )
            }
        }
    }
}
