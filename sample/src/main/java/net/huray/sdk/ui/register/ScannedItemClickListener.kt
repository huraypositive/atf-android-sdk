package net.huray.sdk.ui.register

import net.huray.sdk.model.DiscoveredAtfleeDevice

interface ScannedItemClickListener {
    fun onDeviceClickListener(device: DiscoveredAtfleeDevice)
}