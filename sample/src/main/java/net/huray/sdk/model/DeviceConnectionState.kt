package net.huray.sdk.model

sealed class DeviceConnectionState {
    object Idle : DeviceConnectionState()

    object Scanning : DeviceConnectionState()

    data class Failed(
        val error: AtfleeError
    ) : DeviceConnectionState()

    data class OnScanned(
        val discoveredDevices: List<DiscoveredAtfleeDevice>
    ) : DeviceConnectionState()

    object Connecting : DeviceConnectionState()

    object ConnectionSuccess : DeviceConnectionState()

    data class TransferSuccess(
        val records: List<AtfleeWeightData>
    ) : DeviceConnectionState()

    object NoDataTransferred : DeviceConnectionState()
}
