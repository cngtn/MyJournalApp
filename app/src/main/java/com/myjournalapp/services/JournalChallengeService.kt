package com.myjournalapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.myjournalapp.R

class JournalChallengeService : Service() {

    private val NOTIFICATION_CHANNEL_ID = "journal_challenge_channel"
    private val NOTIFICATION_ID = 123 // Unique ID for the notification

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notification = createNotification("Day 1: What are you grateful for today?")

        startForeground(NOTIFICATION_ID, notification)

        // In a real app, you would start a coroutine to periodically update the notification.
        // For now, this is a static notification.

        return START_STICKY
    }

    private fun createNotification(contentText: String): Notification {
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with a proper icon
            .setContentTitle("30-Day Journal Challenge")
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setOngoing(true) // Crucial for Live Updates
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setRequestPromotedOngoing(true) // Correct way to request promotion
            .setShortCriticalText("Test app")
        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Journal Challenge Updates"
            val descriptionText = "Live updates for your ongoing journal challenges"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // We don't provide binding
    }
}
