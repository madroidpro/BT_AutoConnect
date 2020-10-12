package com.madroid.btautoconnect.adapter

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.madroid.btautoconnect.databinding.LayoutBtDevicesBinding

/**
 * Created by HND6KOR on 10/12/2020.
 */
class DeviceListAdapter(private var device: List<BluetoothDevice>) : RecyclerView.Adapter<DeviceListAdapter.DeviceVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutBtDevicesBinding.inflate(inflater, parent, false)
        return DeviceVH(binding)
    }

    fun update(updatedDevice: List<BluetoothDevice>) {
        device = updatedDevice;
        notifyDataSetChanged();
    }

    override fun getItemCount(): Int {
        return device.size
    }

    override fun onBindViewHolder(holder: DeviceVH, position: Int) {
        holder.bind(device[position])
    }

    inner class DeviceVH(private val binding: LayoutBtDevicesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(device: BluetoothDevice) {
            binding.devices = device
            binding.executePendingBindings()
        }

    }

}