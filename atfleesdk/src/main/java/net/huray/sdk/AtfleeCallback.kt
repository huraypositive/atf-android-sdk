package net.huray.sdk

import net.huray.sdk.model.AtfleeDeviceState
import net.huray.sdk.model.AtfleeError
import net.huray.sdk.model.AtfleeWeightData
import net.huray.sdk.model.DiscoveredAtfleeDevice

interface AtfleeCallback {
    fun onScanned(devices: List<DiscoveredAtfleeDevice>)

    fun onDeviceConnectionChanged(state: AtfleeDeviceState)

    fun onReceiveUnstableWeight(weight: Double)

    fun onStartMeasureWeight()

    fun onCompleteMeasureWeight(data: AtfleeWeightData)

    fun onError(error: AtfleeError)
}
