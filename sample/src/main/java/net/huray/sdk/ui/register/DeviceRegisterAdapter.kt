package net.huray.sdk.ui.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.huray.sdk.R
import net.huray.sdk.model.DiscoveredAtfleeDevice

class DeviceRegisterAdapter(
    private val clickListener: ScannedItemClickListener,
) : RecyclerView.Adapter<DeviceRegisterViewHolder>() {

    private val devices: MutableList<DiscoveredAtfleeDevice> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceRegisterViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_scanned_device,
                parent,
                false
            )

        return DeviceRegisterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceRegisterViewHolder, position: Int) {
        holder.tvName.text = devices[position].name
        holder.tvAddress.text = devices[position].address

        holder.vgDevice.setOnClickListener {
            clickListener.onDeviceClickListener(devices[position])
        }
    }

    override fun getItemCount(): Int = devices.size

    fun updateDevices(devices: List<DiscoveredAtfleeDevice>) {
        this.devices.clear()

        this.devices.addAll(devices)
        notifyItemChanged(this.devices.lastIndex)
    }
}