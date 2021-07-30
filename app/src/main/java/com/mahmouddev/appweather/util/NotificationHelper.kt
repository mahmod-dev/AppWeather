package com.mahmouddev.appweather.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mahmouddev.appweather.R

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private var notification: NotificationCompat.Builder? = null

    init {
         createNotificationChannels()
    }

    companion object {
        const val CHANNEL_MESSAGE = "TEMPERATURE"
        const val CHANNEL_ID = "CHANNEL_TEMPERATURE"

        @Volatile
        private var instance: NotificationHelper? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(context) {
                instance ?: NotificationHelper(context).also {
                    instance = it

                }
            }
    }

    fun createNotification(
        title: String,
        details: String,
    ) {

        val manager = NotificationManagerCompat.from(this)

         notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(details)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setWhen(System.currentTimeMillis())
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(getColor(R.color.teal_700))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)

        val notify = notification?.build()


        manager.notify(1, notify!!)

    }



    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_MESSAGE,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.name = CHANNEL_MESSAGE
            channel.description = "THis channel to get Temperature degree"

            val manager = getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannel(channel)
        }
    }

}