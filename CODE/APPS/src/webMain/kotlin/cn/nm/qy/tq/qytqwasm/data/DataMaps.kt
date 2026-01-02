package cn.nm.qy.tq.qytqwasm.data

import cn.nm.qy.tq.qytqwasm.cytq.CytqOwns
import cn.nm.qy.tq.qytqwasm.cytq.CytqOwns.Result.Daily.AirQuality.Aqi
import cn.nm.qy.tq.qytqwasm.cytq.CytqOwns.Result.Daily.AirQuality.Pm25
import cn.nm.qy.tq.qytqwasm.cytq.CytqOwns.Result.Daily.Astro
import cn.nm.qy.tq.qytqwasm.cytq.CytqOwns.Result.Hourly.*

data class HourMaps(
    val precipitation: Precipitation,/*降水*/
    val temperature: Temperature,/*温度*/
    val wind: Wind,/*风的*/
    val humidity: Humidity,/*湿度*/
    val cloudrate: Cloudrate,/*云率*/
    val skycon: Skycon,/*天气现象*/
    val pressure: Pressure,/*气压*/
    val visibility: Visibility,/*可见距离*/
    val dswrf: Dswrf,/*向下短波辐射通量*/
)

fun CytqOwns.Result.Hourly.hourly(): List<HourMaps> {
    return List(
        listOf(
            precipitation.size,
            temperature.size,
            wind.size,
            humidity.size,
            cloudrate.size,
            skycon.size,
            pressure.size,
            visibility.size,
            dswrf.size
        ).minOrNull() ?: 0
    ) {
        HourMaps(
            precipitation = precipitation[it],
            temperature = temperature[it],
            wind = wind[it],
            humidity = humidity[it],
            cloudrate = cloudrate[it],
            skycon = skycon[it],
            pressure = pressure[it],
            visibility = visibility[it],
            dswrf = dswrf[it]
        )
    }
}





data class AirsMaps(
    val aqi: Aqi?,
    val pm25: Pm25?
)

data class DaysMaps(
    val astro: Astro,/*天文*/
    val precipitation: CytqOwns.Result.Daily.Precipitation,/*降水*/
    val temperature: CytqOwns.Result.Daily.Temperature,/*温度*/
    val wind: CytqOwns.Result.Daily.Wind,/*风的*/
    val humidity: CytqOwns.Result.Daily.Humidity,/*湿度*/
    val cloudrate: CytqOwns.Result.Daily.Cloudrate,/*云量*/
    val pressure: CytqOwns.Result.Daily.Pressure,/*气压*/
    val visibility: CytqOwns.Result.Daily.Visibility,/*地表水平的能见度*/
    val dswrf: CytqOwns.Result.Daily.Dswrf,/*向下短波辐射通量*/
    val skycon: CytqOwns.Result.Daily.Skycon,/*全天主要天气现象*/
    val skycon08h20h: CytqOwns.Result.Daily.Skycon,/*白天主要天气现象*/
    val skycon20h32h: CytqOwns.Result.Daily.Skycon,/*夜晚主要天气现象*/
    val airQuality: AirsMaps
)

fun CytqOwns.Result.Daily.daily(): List<DaysMaps> {
    return List(
        listOf(
            astro.size,
            precipitation.size,
            temperature.size,
            wind.size,
            humidity.size,
            cloudrate.size,
            pressure.size,
            visibility.size,
            dswrf.size,
            skycon.size,
            skycon08h20h.size,
            skycon20h32h.size,
        ).minOrNull() ?: 0
    ) {
        DaysMaps(
            astro = astro[it],
            precipitation = precipitation[it],
            temperature = temperature[it],
            wind = wind[it],
            humidity = humidity[it],
            cloudrate = cloudrate[it],
            pressure = pressure[it],
            visibility = visibility[it],
            dswrf = dswrf[it],
            skycon = skycon[it],
            skycon08h20h = skycon08h20h[it],
            skycon20h32h = skycon20h32h[it],
            airQuality =
                AirsMaps(
                    aqi = airQuality.aqi.getOrNull(it),
                    pm25 = airQuality.pm25.getOrNull(it)
                )
        )
    }
}
