package com.mahmouddev.appweather.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mahmouddev.appweather.util.AlarmHelper

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED)
            AlarmHelper.startAlarm(context)
    }


}