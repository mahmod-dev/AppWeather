package com.mahmouddev.appweather.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mahmouddev.appweather.receiver.TempReceiver
import com.mahmouddev.appweather.util.Constants.DAILY
import java.util.*


object AlarmHelper {

    fun startAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, TempReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
       // context.sendBroadcast(intent)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager!!.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            DAILY, // repeating every 24 hour
            pendingIntent
        )
    }

}