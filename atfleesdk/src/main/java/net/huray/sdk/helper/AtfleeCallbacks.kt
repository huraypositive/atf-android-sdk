package net.huray.sdk.helper

import android.util.Log
import cn.icomon.icdevicemanager.ICDeviceManagerDelegate
import cn.icomon.icdevicemanager.model.data.*
import cn.icomon.icdevicemanager.model.device.ICDevice
import cn.icomon.icdevicemanager.model.device.ICDeviceInfo
import cn.icomon.icdevicemanager.model.other.ICConstant
import net.huray.sdk.AtfleeCallback
import net.huray.sdk.model.AtfleeDeviceState

internal const val TAG = "[AtfleeSDK]"

internal fun initDeviceCallback(
    atfleeCallback: AtfleeCallback,
    deviceAddress: String?
) = object : ICDeviceManagerDelegate {
        override fun onInitFinish(p0: Boolean) {
            Log.d(TAG, "onInitFinish Success")
        }

        override fun onBleState(p0: ICConstant.ICBleState?) {
            Log.d(TAG, "onBleState :: $p0")
        }

        override fun onDeviceConnectionChanged(
            device: ICDevice?,
            state: ICConstant.ICDeviceConnectState?
        ) {
            Log.d(TAG, "onDeviceConnectionChanged :: $state")

            if (device?.macAddr != deviceAddress) return

            val connectionState = convertConnectionStateFrom(state)
            atfleeCallback.onDeviceConnectionChanged(connectionState)
        }

        override fun onReceiveMeasureStepData(
            device: ICDevice?,
            step: ICConstant.ICMeasureStep?,
            data: Any?
        ) {
            Log.d(TAG, "onReceiveMeasureStepData :: $step")

            if (device?.macAddr != deviceAddress) return

            when (step) {
                ICConstant.ICMeasureStep.ICMeasureStepMeasureWeightData -> {
                    val weight: ICWeightData = data as ICWeightData
                    atfleeCallback.onReceiveUnstableWeight(weight.weight_kg)
                }
                ICConstant.ICMeasureStep.ICMeasureStepAdcStart -> {
                    atfleeCallback.onStartMeasureWeight()
                }
                ICConstant.ICMeasureStep.ICMeasureStepAdcResult -> {
//                    val deviceData = data as ICWeightData
//                    val weightData = convertWeightDataFrom(deviceData)
//                    atfleeCallback.onCompleteMeasureWeight(weightData)
                }
                ICConstant.ICMeasureStep.ICMeasureStepMeasureOver -> {
                    val deviceData = data as ICWeightData
                    val weightData = convertWeightDataFrom(deviceData)
                    atfleeCallback.onCompleteMeasureWeight(weightData)
                }
                else -> {}
            }
        }

        override fun onReceiveWeightData(p0: ICDevice?, p1: ICWeightData?) {
            Log.d(TAG, "onReceiveWeightData :: ${p1?.weight_kg}")
        }

        override fun onReceiveKitchenScaleData(p0: ICDevice?, p1: ICKitchenScaleData?) {
        }

        override fun onReceiveKitchenScaleUnitChanged(
            p0: ICDevice?,
            p1: ICConstant.ICKitchenScaleUnit?
        ) {
        }

        override fun onReceiveCoordData(p0: ICDevice?, p1: ICCoordData?) {
        }

        override fun onReceiveRulerData(p0: ICDevice?, p1: ICRulerData?) {
        }

        override fun onReceiveRulerHistoryData(p0: ICDevice?, p1: ICRulerData?) {
        }

        override fun onReceiveWeightCenterData(p0: ICDevice?, p1: ICWeightCenterData?) {
        }

        override fun onReceiveWeightUnitChanged(p0: ICDevice?, p1: ICConstant.ICWeightUnit?) {
        }

        override fun onReceiveRulerUnitChanged(p0: ICDevice?, p1: ICConstant.ICRulerUnit?) {
        }

        override fun onReceiveRulerMeasureModeChanged(
            p0: ICDevice?,
            p1: ICConstant.ICRulerMeasureMode?
        ) {
        }

        override fun onReceiveWeightHistoryData(p0: ICDevice?, p1: ICWeightHistoryData?) {
        }

        override fun onReceiveSkipData(p0: ICDevice?, p1: ICSkipData?) {
        }

        override fun onReceiveHistorySkipData(p0: ICDevice?, p1: ICSkipData?) {
        }

        override fun onReceiveSkipBattery(p0: ICDevice?, p1: Int) {
        }

        override fun onReceiveUpgradePercent(
            p0: ICDevice?,
            p1: ICConstant.ICUpgradeStatus?,
            p2: Int
        ) {
        }

        override fun onReceiveDeviceInfo(p0: ICDevice?, p1: ICDeviceInfo?) {
        }

        override fun onReceiveDebugData(p0: ICDevice?, p1: Int, p2: Any?) {
        }

        override fun onReceiveConfigWifiResult(
            p0: ICDevice?,
            p1: ICConstant.ICConfigWifiState?
        ) {
        }
    }
