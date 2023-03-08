package net.huray.sdk.ui.register

import android.os.Bundle
import android.widget.Toast
import net.huray.sdk.R
import net.huray.sdk.common.BaseActivity
import net.huray.sdk.databinding.ActivityDeviceRegisterBinding
import net.huray.sdk.model.AtfleeDeviceType
import net.huray.sdk.model.AtfleeError
import net.huray.sdk.model.DeviceConnectionState
import net.huray.sdk.model.DiscoveredAtfleeDevice
import net.huray.sdk.utils.Const

class DeviceRegisterActivity : BaseActivity(), ScannedItemClickListener {
    private lateinit var binding: ActivityDeviceRegisterBinding

    private lateinit var adapter: DeviceRegisterAdapter
    private lateinit var deviceType: AtfleeDeviceType

    private val viewModel: DeviceRegisterViewModel by viewModelsFactory {
        val deviceId = intent.getIntExtra(Const.EXTRA_DEVICE_TYPE, 0)
        deviceType = AtfleeDeviceType.fromId(deviceId)

        DeviceRegisterViewModel(deviceType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initObservers()
        initViews()
    }

    override fun onDeviceClickListener(device: DiscoveredAtfleeDevice) {
        viewModel.connectDevice(address = device.address)
    }

    private fun initObservers() {
        viewModel.connectionEvent.observe(this) { state ->
            when (state) {
                is DeviceConnectionState.Scanning -> handleScanningEvent()
                is DeviceConnectionState.OnScanned -> adapter.updateDevices(state.discoveredDevices)
                is DeviceConnectionState.Failed -> handleCancelEvent(state.error)
                is DeviceConnectionState.ConnectionSuccess -> handleSuccessEvent()
                else -> {}
            }
        }
    }

    private fun initViews() {
        adapter = DeviceRegisterAdapter(this)

        binding.tvScanTitle.text = "${deviceType.modelName} 스캔"
        binding.rvScannedDeviceList.adapter = adapter
    }

    private fun handleCancelEvent(error: AtfleeError) {
        Toast.makeText(this, error.name, Toast.LENGTH_SHORT).show()
        setViewForReadyToScan()
    }

    private fun handleScanningEvent() {
        binding.tvScanDescription.text = getString(R.string.scanning_device)
    }

    private fun handleSuccessEvent() {
        Toast.makeText(
            this,
            getString(R.string.connection_success),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    private fun setViewForReadyToScan() {
        binding.btnScan.text = getString(R.string.start_scan_device)
        binding.tvScanDescription.text = getString(R.string.click_device_scan_button)
    }
}