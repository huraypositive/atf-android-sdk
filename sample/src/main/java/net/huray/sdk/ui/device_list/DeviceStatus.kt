package net.huray.sdk.ui.device_list

import net.huray.sdk.model.AtfleeDeviceType

data class DeviceStatus(
    val deviceType: AtfleeDeviceType,
    val isConnected: Boolean,
)