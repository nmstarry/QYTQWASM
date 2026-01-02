package cn.nm.qy.tq.qytqwasm.mold

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.nm.qy.tq.qytqwasm.cytq.AppsData
import cn.nm.qy.tq.qytqwasm.cytq.CytqCity
import cn.nm.qy.tq.qytqwasm.cytq.CytqData
import cn.nm.qy.tq.qytqwasm.page.LoadMent
import cn.nm.qy.tq.qytqwasm.tool.HttpTool
import cn.nm.qy.tq.qytqwasm.tool.UserStor
import cn.nm.qy.tq.qytqwasm.tool.showlogs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class ViewMold : ViewModel() {

    val gaindata: StateFlow<AppsData> field = MutableStateFlow(AppsData())

    fun trimdata(call: (data: AppsData) -> AppsData) {
        gaindata.update { call(it) }
    }

    init {
        gaindata()
    }

    fun gaindata() {
        viewModelScope.launch {
            UserStor.loaddata().also { data ->
                gaindata.update {
                    if (data == null) {
                        AppsData(accepted = false)
                    } else {
                        if (data.accepted == null) {
                            data.copy(accepted = false)
                        } else {
                            data
                        }
                    }
                }
            }
        }
    }

    fun savedata(data: AppsData) {
        viewModelScope.launch {
            UserStor.savedata(data = data)
            gaindata()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}


class LoadMold : ViewModel() {

    var loadment by mutableStateOf(LoadMent.ACTSSTOP)

    fun gainowns(viewmold: ViewMold, loadsite: CytqCity.Places, appsdata: AppsData) {
        viewModelScope.launch {
            loadment = LoadMent.DATALOAD
            HttpTool.gainowns(
                loadsite.location.lng,
                loadsite.location.lat,
            ) { gain ->
                showlogs(gain)
                if (gain.gainment == true && gain.gaindata != null) {
                    viewmold.savedata(
                        appsdata.copy(
                            cytqlist = appsdata.cytqlist.plus(
                                CytqData(
                                    serially = if (appsdata.cytqlist.isNotEmpty()) {
                                        appsdata.cytqlist.maxOf { it.serially }
                                    } else {
                                        0
                                    } + 1, maketime = Clock.System.now().toEpochMilliseconds(), cytqcity = loadsite, cytqowns = gain.gaindata))))
                    loadment = LoadMent.DATADONE
                } else {
                    loadment = LoadMent.DATAFAIL
                }
            }
        }
    }
}
