package cn.nm.qy.tq.qytqwasm.tool

import cn.nm.qy.tq.qytqwasm.cytq.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object HttpTool {

    val androidx: String = ""
    val userhttp: String = ""

    @OptIn(ExperimentalSerializationApi::class)
    val clientio = HttpClient {
        install(HttpRequestRetry) {
            retryOnExceptionOrServerErrors()
            exponentialDelay()
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true

                allowTrailingComma = true
                isLenient = true
            })
        }
        install(DefaultRequest) {
            headers {
                set(HttpHeaders.UserAgent, userhttp)
            }
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    //message.showlogs()
                }
            }
            level = LogLevel.ALL
        }
    }

    suspend fun gaincity(cityname: String, callback: (GainCity) -> Unit) {
        //val httpurls = "https://api.caiyunapp.com/v2/place"
        val httpurls = "/xxxxx/v2/place"

        runCatching {
            clientio.get(httpurls) {
                url {
                    parameters.append("query", cityname)
                    parameters.append("lang", CYTQZHCN)
                    parameters.append("token", CYTQKEYS)
                    parameters.append("count", "10")
                }
            }.also { gain ->
                runCatching { gain.body<CytqCity>().places }.fold({
                    callback(GainCity(GainMent.DONE, it))
                }, {
                    callback(GainCity(GainMent.NONE))
                })
            }
        }.onFailure {
            callback(GainCity(GainMent.OFFS))
        }
    }

    suspend fun gainowns(
        horizons: Double, vertical: Double, callback: suspend (GainOwns) -> Unit
    ) {
        //val httpurls = "https://api.caiyunapp.com/v2.5/${CYTQKEYS}/$horizons,$vertical/weather"
        val httpurls = "/zzzzz/v2.5/${CYTQKEYS}/$horizons,$vertical/weather"

        runCatching {
            clientio.get(httpurls) {
                url {
                    parameters.append("lang", CYTQZHCN)
                    parameters.append("span", "16")
                    parameters.append("dailystart", "0")
                    parameters.append("hourlysteps", "24")
                    parameters.append("alert", "true")
                    parameters.append("version", "6.0.0")
                    parameters.append("device_id", androidx)
                }
            }.also {
                callback(
                    GainOwns(
                        it.status.isSuccess(), runCatching { it.body<CytqOwns>() }.getOrNull()
                    )
                )
            }
        }.onFailure {
            callback(GainOwns())
        }
    }
}