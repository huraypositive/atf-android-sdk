package net.huray.sdk.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.huray.sdk.App
import net.huray.sdk.AtfleeCallback
import net.huray.sdk.AtfleeManager
import net.huray.sdk.model.*
import net.huray.sdk.utils.PrefUtils

class DeviceRegisterViewModel(
    private val deviceType: AtfleeDeviceType
) : ViewModel(), AtfleeCallback {

    private val _connectionEvent = MutableLiveData<DeviceConnectionState>()
    val connectionEvent: LiveData<DeviceConnectionState> get() = _connectionEvent

    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean> get() = _loadingEvent

    private val atfleeManager = AtfleeManager.getInstance(App.instance, this)

    override fun onCleared() {
        super.onCleared()
        atfleeManager.dispose()
    }

    override fun onScanned(devices: List<DiscoveredAtfleeDevice>) {
        _connectionEvent.postValue(DeviceConnectionState.OnScanned(devices))
    }

    override fun onDeviceConnectionChanged(state: AtfleeDeviceState) {
        /* not use */
    }

    override fun onReceiveUnstableWeight(weight: Double) {
        /* not use */
    }

    override fun onCompleteMeasureWeight(data: AtfleeWeightData) {
        /* not use */
    }

    override fun onStartMeasureWeight() {
        /* not use */
    }

    override fun onError(error: AtfleeError) {
        setLoadingState(false)
        _connectionEvent.postValue(DeviceConnectionState.Failed(error))
    }

    fun startScan() {
        atfleeManager.startScan(listOf(deviceType))
        _connectionEvent.value = DeviceConnectionState.Scanning
    }

    fun stopScan() {
        atfleeManager.stopScan()
        _connectionEvent.value = DeviceConnectionState.Idle
    }

    fun connectDevice(address: String) {
        stopScan()
        PrefUtils.saveDeviceAddress(address, deviceType)

        _connectionEvent.postValue(DeviceConnectionState.ConnectionSuccess)
    }

    private fun setLoadingState(isLoading: Boolean) {
        _loadingEvent.postValue(isLoading)
    }
}