package com.madroid.btautoconnect.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import android.view.KeyEvent
import java.util.*
import kotlin.concurrent.schedule

class BTBroadcastReceiver : BroadcastReceiver() {
    val TAG = "BTBroadcastReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "${intent.action}")
        when (intent.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                Log.d(TAG, "${device?.name} connected")
                Timer("starting", false).schedule(500) {
                    val audioManager: AudioManager =
                        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    val downEvent =
                        KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                    val upEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                    audioManager.dispatchMediaKeyEvent(downEvent)
                    audioManager.dispatchMediaKeyEvent(upEvent)
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