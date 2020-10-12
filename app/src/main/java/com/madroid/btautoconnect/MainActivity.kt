package com.madroid.btautoconnect

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.madroid.btautoconnect.adapter.DeviceListAdapter
import com.madroid.btautoconnect.common.CommonUtils.Companion.SAVED_BT_DEVICES
import com.madroid.btautoconnect.common.CommonUtils.Companion.SHARED_PREF
import com.madroid.btautoconnect.databinding.ActivityMainBinding
import com.madroid.btautoconnect.service.BTService
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    var deviceList: List<BluetoothDevice> = ArrayList()
    private val TAG = "MainActivity"
    lateinit var deviceListAdapter: DeviceListAdapter
    lateinit var sharedPreferences: SharedPreferences
    var savedDevices: MutableSet<String>? = mutableSetOf()
    lateinit var mBinding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.presenter = this
        initView()
        startAction();
    }

    private fun initView() {
        sharedPreferences = this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        savedDevices = sharedPreferences.getStringSet(SAVED_BT_DEVICES, null);
        deviceListAdapter = DeviceListAdapter(deviceList, this);
        mBinding.rcList.adapter = deviceListAdapter
        mBinding.rcList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    /*Listen for checkbox*/
    fun onCheckedChange(checked: Boolean, address: String) {
        Log.d(TAG, "$checked add --> $address")
        Log.d(TAG, savedDevices.toString())
        if (checked) {
            val set: MutableSet<String> = mutableSetOf()
            savedDevices?.let { set.addAll(it) }
            set.add(address)
            val editer = sharedPreferences.edit()
            editer.putStringSet(SAVED_BT_DEVICES, set).apply()
            editer.putStringSet(SAVED_BT_DEVICES, set).commit()
            Log.d(TAG, sharedPreferences.getStringSet(SAVED_BT_DEVICES, null).toString())
        } else {
            val set: MutableSet<String> = mutableSetOf()
            savedDevices?.asIterable()?.let { set.addAll(it) }
            set.remove(address)
            val editer = sharedPreferences.edit()
            editer.putStringSet(SAVED_BT_DEVICES, set).apply()
            editer.putStringSet(SAVED_BT_DEVICES, set).commit()
            Log.d(TAG, sharedPreferences.getStringSet(SAVED_BT_DEVICES, null).toString())
        }

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
                val deviceName = d.name;
                val macAddress = d.address;
                Log.d(TAG, "paired device: $deviceName at $macAddress")
            }
            BTService.startService(this, "Active")
        }
    }

    /*Stop BT listen action */
    fun stopAction() {
        BTService.stopService(this)
    }
}