package net.huray.sdk.ui.device_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.huray.sdk.databinding.ItemDeviceListBinding

class DeviceListViewHolder(
    containerView: View,
) : RecyclerView.ViewHolder(containerView) {
    private val binding = ItemDeviceListBinding.bind(containerView)
    val viewGroup = binding.vgDeviceItem
    val tvDevice = binding.tvDeviceItem
    val ivConnectionIndicator = binding.ivConnectionIndicator
}