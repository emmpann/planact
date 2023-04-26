package com.efan.planact

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.efan.planact.util.channelID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlanactApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    @SuppressLint("WrongConstant")
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notif channel"
            val desc = "A description of the channel"
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel(channelID, name, importance)
            channel.description = desc
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}