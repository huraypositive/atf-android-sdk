package net.huray.sdk

import android.content.Context
import android.util.Log
import cn.icomon.icdevicemanager.ICDeviceManager
import cn.icomon.icdevicemanager.model.device.ICDevice
import cn.icomon.icdevicemanager.model.device.ICUserInfo
import cn.icomon.icdevicemanager.model.other.ICConstant
import cn.icomon.icdevicemanager.model.other.ICDeviceManagerConfig
import net.huray.sdk.helper.*
import net.huray.sdk.helper.TAG
import net.huray.sdk.helper.convertErrorFrom
import net.huray.sdk.helper.getBfaTypeFrom
import net.huray.sdk.helper.getUserGenderFrom
import net.huray.sdk.helper.initDeviceCallback
import net.huray.sdk.model.AtfleeDeviceType
import net.huray.sdk.model.AtfleeUser
import net.huray.sdk.model.DiscoveredAtfleeDevice

class AtfleeManager(
    private val context: Context,
    private val atfleeCallback: AtfleeCallback,
) : AtfleeBleDeviceManager {

    init {
        initSdk()
    }

    override fun startScan(deviceTypes: List<AtfleeDeviceType>) {
        stopScan()

        val discoveredDevices = mutableSetOf<DiscoveredAtfleeDevice>()
        val addresses = mutableListOf<String>()

        ICDeviceManager.shared().scanDevice { device ->
            Log.d(TAG, "Discovered Device = ${device?.name} :: ${device?.macAddr}")

            val types: List<AtfleeDeviceType> =
                deviceTypes.ifEmpty { AtfleeDeviceType.values().toList() }

            if (types.map { it.scanName }.contains(device?.name).not()) return@scanDevice

            if (addresses.contains(device.macAddr)) return@scanDevice

            val modelName = getModelNameFrom(device.name, types)
            val discovered = DiscoveredAtfleeDevice(modelName, device.macAddr)
            addresses.add(device.macAddr)
            discoveredDevices.add(discovered)
            atfleeCallback.onScanned(discoveredDevices.toList())
        }
    }

    override fun stopScan() {
        ICDeviceManager.shared().stopScan()
    }

    override fun startMeasureMode(
        deviceAddress: String,
        user: AtfleeUser
    ) {
        val connectionCallback = ICConstant.ICAddDeviceCallBack { _, code ->
            Log.d(TAG, "ICAddDeviceCallBack : $code")
            when (code) {
                ICConstant.ICAddDeviceCallBackCode.ICAddDeviceCallBackCodeSuccess -> {
                    initDeviceCallback(deviceAddress)
                    updateUserInfo(user)
                }
                else -> {
                    val error = convertErrorFrom(code)
                    atfleeCallback.onError(error)
                }
            }
        }

        val device = ICDevice().apply { macAddr = deviceAddress }
        ICDeviceManager.shared().addDevice(device, connectionCallback)
    }

    override fun disconnect(address: String) {
        val device = ICDevice().apply { macAddr = address }
        ICDeviceManager.shared().removeDevice(device) { _, code ->
            Log.d(TAG, "removeDevice Callback code: $code")
        }
    }

    override fun dispose() {
        ICDeviceManager.shared().deInit()
    }

    private fun initSdk() {
        val config = ICDeviceManagerConfig()
        config.context = context
        ICDeviceManager.shared().initMgrWithConfig(config)
    }

    private fun initDeviceCallback(
        deviceAddress: String,
    ) {
        val deviceCallback = initDeviceCallback(atfleeCallback, deviceAddress)
        ICDeviceManager.shared().delegate = deviceCallback
    }

    private fun updateUserInfo(user: AtfleeUser) {
        val userData = ICUserInfo()
        userData.age = user.age
        userData.height = user.height
        userData.sex = getUserGenderFrom(user.gender)
        userData.bfaType = getBfaTypeFrom(user.deviceType)
        userData.peopleType = ICConstant.ICPeopleType.ICPeopleTypeNormal

        ICDeviceManager.shared().updateUserInfo(userData)
    }

    companion object {
        fun getInstance(
            context: Context,
            callback: AtfleeCallback,
        ): AtfleeBleDeviceManager {
            return AtfleeManager(context, callback)
        }
    }
}

interface AtfleeBleDeviceManager {
    fun startScan(deviceTypes: List<AtfleeDeviceType>)

    fun stopScan()

    fun startMeasureMode(deviceAddress: String, user: AtfleeUser)

    fun disconnect(address: String)

    fun dispose()
}
