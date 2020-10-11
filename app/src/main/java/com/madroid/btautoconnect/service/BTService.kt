package com.madroid.btautoconnect.service

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.madroid.btautoconnect.receiver.BTBroadcastReceiver

class BTService : Service() {
    val TAG = "BTService"
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d(TAG, "Registered service")
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intentFilter.addAction(ACTION_ACL_DISCONNECTED)
        registerReceiver(BTBroadcastReceiver(), intentFilter)
    }

    override fun onDestroy() {
        Log.d(TAG, "UnRegistered service")
        unregisterReceiver(BTBroadcastReceiver())
    }
}