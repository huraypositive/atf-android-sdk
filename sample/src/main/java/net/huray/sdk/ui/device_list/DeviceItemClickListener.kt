package net.huray.sdk.ui.device_list

interface DeviceItemClickListener {
    fun onItemClicked(isConnected: Boolean, deviceNumber: Int)
}