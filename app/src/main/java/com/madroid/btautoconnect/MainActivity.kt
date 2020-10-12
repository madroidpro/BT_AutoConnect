package com.madroid.btautoconnect

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.madroid.btautoconnect.adapter.DeviceListAdapter
import com.madroid.btautoconnect.databinding.ActivityMainBinding
import com.madroid.btautoconnect.service.BTService
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    var deviceList: List<BluetoothDevice> = ArrayList()
    lateinit var deviceListAdapter: DeviceListAdapter
    lateinit var mBinding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.presenter = this
        initView()
        startAction();
    }

    private fun initView() {
        deviceListAdapter = DeviceListAdapter(deviceList);
        mBinding.rcList.adapter = deviceListAdapter
        mBinding.rcList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }


    /*Start action on button press*/
    fun startAction() {
        mBinding.progressbar.visibility = View.VISIBLE
        if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
            showBtDevices()
        } else {
            mBinding.progressbar.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.alert)
            builder.setMessage(R.string.bt_disabled)
            builder.setPositiveButton(R.string.yes) { _, _ ->
                BluetoothAdapter.getDefaultAdapter().enable()
                mBinding.progressbar.visibility = View.VISIBLE
                Timer("Refresh BT", false).schedule(3000) {
                    runOnUiThread {
                        showBtDevices()
                    }
                }
            }
            builder.setNegativeButton(R.string.no, null)
            builder.show()
            builder.setCancelable(true)
        }
    }

    private fun showBtDevices() {
        mBinding.progressbar.visibility = View.GONE
        val pairedDevices = BluetoothAdapter.getDefaultAdapter().bondedDevices;
        if (pairedDevices.size > 0) {
            deviceListAdapter.update(pairedDevices.toList())
            for (d in pairedDevices) {
                var deviceName = d.name;
                var macAddress = d.address;
                Log.d("info_devices", "paired device: $deviceName at $macAddress")
            }
            BTService.startService(this, "Active")
        }
    }
}