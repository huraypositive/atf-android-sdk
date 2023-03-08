package net.huray.sdk.ui.transfer

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import net.huray.sdk.R
import net.huray.sdk.common.BaseActivity
import net.huray.sdk.databinding.ActivityRequestDataBinding
import net.huray.sdk.model.AtfleeDeviceType
import net.huray.sdk.model.AtfleeError
import net.huray.sdk.model.DeviceConnectionState
import net.huray.sdk.utils.Const

class DeviceTransferActivity : BaseActivity() {
    private lateinit var binding: ActivityRequestDataBinding

    private lateinit var deviceType: AtfleeDeviceType

    private val viewModel: DeviceTransferViewModel by viewModelsFactory {
        val deviceId = intent.getIntExtra(Const.EXTRA_DEVICE_TYPE, 0)
        deviceType = AtfleeDeviceType.fromId(deviceId)

        DeviceTransferViewModel(deviceType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initViews()
        initObservers()
    }

    private fun initViews() {
        binding.tvRequestTitle.text = "${deviceType.modelName} 측정"
        binding.tvDisconnectDevice.setOnClickListener { showConfirmDialog() }
    }

    private fun initObservers() {
        viewModel.connectionEvent.observe(this) { state ->
            when (state) {
                is DeviceConnectionState.Failed -> handleFailureEvent(state.error)
                is DeviceConnectionState.NoDataTransferred -> {
                    Toast.makeText(
                        this,
                        getString(R.string.no_data_to_bring),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is DeviceConnectionState.TransferSuccess -> {
                    Toast.makeText(
                        this,
                        getString(R.string.success_to_receive_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }

    private fun handleFailureEvent(error: AtfleeError) {
        Toast.makeText(
            this,
            error.name,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showConfirmDialog() {
        val dialog = AlertDialog.Builder(this).create()
        dialog.setTitle(getString(R.string.alert))
        dialog.setMessage(getString(R.string.sure_to_disconnect))

        dialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            getString(R.string.cacel)
        ) { _: DialogInterface?, _: Int -> dialog.dismiss() }

        dialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            getString(R.string.disconnect)
        ) { _: DialogInterface?, _: Int ->
            viewModel.disconnectDevice()
            finish()
            dialog.dismiss()
        }

        dialog.show()
    }
}