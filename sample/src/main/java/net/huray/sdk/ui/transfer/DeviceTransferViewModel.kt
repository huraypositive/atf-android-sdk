package net.huray.sdk.ui.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.huray.sdk.App
import net.huray.sdk.AtfleeCallback
import net.huray.sdk.AtfleeManager
import net.huray.sdk.model.*
import net.huray.sdk.utils.PrefUtils
import kotlin.math.roundToInt

class DeviceTransferViewModel(
    private val deviceType: AtfleeDeviceType
) : ViewModel(), AtfleeCallback {

    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean> get() = _loadingEvent

    private val _connectionEvent = MutableLiveData<DeviceConnectionState>()
    val connectionEvent: LiveData<DeviceConnectionState> get() = _connectionEvent

    private val _connectionState = MutableLiveData("연결을 위해 체중계 위에 발을 살짝 올려주세요.")
    val connectionState: LiveData<String> get() = _connectionState

    private val _measureState = MutableLiveData<String>()
    val measureState: LiveData<String> get() = _measureState

    private val _weightResult = MutableLiveData<String>()
    val weightResult: LiveData<String> get() = _weightResult

    private val atfleeManager = AtfleeManager.getInstance(App.instance, this)

    init {
        startMeasure()
    }

    override fun onCleared() {
        super.onCleared()
        atfleeManager.dispose()
    }

    override fun onScanned(devices: List<DiscoveredAtfleeDevice>) {
        /* not use */
    }

    override fun onDeviceConnectionChanged(state: AtfleeDeviceState) {
        val connectionState =
            if (state == AtfleeDeviceState.CONNECTED) "연결되었습니다" else "연결이 끊어졌습니다"
        _connectionState.postValue(connectionState)
    }

    override fun onReceiveUnstableWeight(weight: Double) {
        _measureState.postValue("${weight.roundToInt()}kg")
    }

    override fun onStartMeasureWeight() {
        _measureState.postValue("측정중입니다..")
    }

    override fun onCompleteMeasureWeight(data: AtfleeWeightData) {
        _measureState.postValue("측정 완료")
        _weightResult.postValue(data.toString())
    }

    override fun onError(error: AtfleeError) {
    }

    fun disconnectDevice() {
        val address = PrefUtils.getDeviceAddress(deviceType)
        if (address != null) {
            atfleeManager.disconnect(address)
            PrefUtils.removeDeviceAddress(deviceType)
        }
    }

    private fun startMeasure() {
        val address = PrefUtils.getDeviceAddress(deviceType)
        val user = AtfleeUser(25, 175, 60.0, AtfleeUserGender.MALE, deviceType)

        if (address != null) {
            atfleeManager.startMeasureMode(address, user)
        }
    }
}