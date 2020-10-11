package com.madroid.btautoconnect

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.madroid.btautoconnect.databinding.ActivityMainBinding
import com.madroid.btautoconnect.service.BTService

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.presenter = this
    }


    /*Start action on button press*/
    fun startAction() {
        var pairedDevices = BluetoothAdapter.getDefaultAdapter().bondedDevices;
        if (pairedDevices.size > 0) {
            for (d in pairedDevices) {
                var deviceName = d.name;
                var macAddress = d.address;
                Log.d("info_devices", "paired device: $deviceName at $macAddress")
                // do what you need/want this these list items
            }
            val intent = Intent(
                this, BTService::class.java
            )
            startService(intent)
        }
    }
}