package cn.nm.qy.tq.qytqwasm.tool

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cn.nm.qy.tq.qytqwasm.cytq.AppsData
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath

@OptIn(ExperimentalSerializationApi::class)
val jsontool: Json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true

    allowTrailingComma = true
    isLenient = true
}

object StorKeys {
    val USERDATA = stringPreferencesKey("USERNAME")
}

/*无持久化*/
object AppsStor {

    private val datastor = PreferenceDataStoreFactory.createWithPath(produceFile = { "appsdata.preferences_pb".toPath() })

    suspend fun savedata(username: String) {
        datastor.edit { preferences ->
            preferences[StorKeys.USERDATA] = username
        }
    }

    @Composable
    fun gaindata(defaults: String): State<String> {
        return datastor.data.map { preferences ->
            preferences[StorKeys.USERDATA] ?: defaults
        }.collectAsState(defaults)
    }

    suspend fun gaindata(defaults: String, callback: (String) -> Unit) {
        datastor.data.map { preferences ->
            preferences[StorKeys.USERDATA] ?: defaults
        }.catch {}.collectLatest { callback(it) }
    }
}










object UserStor {

    const val USERKEYS = "USERDATA"

    fun savedata(keys: String = USERKEYS, data: AppsData) {
        localStorage.setItem(keys, jsontool.encodeToString(data))
    }

    fun loaddata(keys: String = USERKEYS): AppsData? {
        return localStorage.getItem(keys)?.let {
            runCatching { jsontool.decodeFromString<AppsData>(it) }.getOrNull()
        }
    }

    fun killdata(keys: String) {
        localStorage.removeItem(keys)
    }
}