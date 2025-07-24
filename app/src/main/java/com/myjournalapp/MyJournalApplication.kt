package com.myjournalapp

import android.app.Application
import android.content.Context
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp
import com.myjournalapp.notifications.MeditationNotificationManager

@HiltAndroidApp
class MyJournalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        MeditationNotificationManager.initialize(applicationContext, notificationManager)
    }
}
