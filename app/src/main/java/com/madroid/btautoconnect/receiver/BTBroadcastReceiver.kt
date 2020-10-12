package com.madroid.btautoconnect.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import android.view.KeyEvent
import com.madroid.btautoconnect.common.CommonUtils
import java.util.*
import kotlin.concurrent.schedule

class BTBroadcastReceiver : BroadcastReceiver() {
    val TAG = "BTBroadcastReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences = context.getSharedPreferences(CommonUtils.SHARED_PREF, Context.MODE_PRIVATE)
        val savedDevices = sharedPreferences.getStringSet(CommonUtils.SAVED_BT_DEVICES, null) as? MutableSet<String>;
        Log.d(TAG, "${intent.action}")
        when (intent.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                Log.d(TAG, "${device?.name} connected")
                Log.d(TAG, savedDevices.toString())
                if (savedDevices?.contains(device?.address) == true) {
                    Timer("starting", false).schedule(3000) {
                        val audioManager: AudioManager =
                                context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                        val downEvent =
                                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                        val upEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                        audioManager.dispatchMediaKeyEvent(downEvent)
                        audioManager.dispatchMediaKeyEvent(upEvent)
                    }
                }

            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                Log.d(TAG, "${device?.name} Disconnected")
            }
        }
    }

}