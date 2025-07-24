package com.myjournalapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.ProgressStyle
import androidx.core.graphics.drawable.IconCompat
import com.myjournalapp.R
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.enums.EnumEntries
import kotlin.math.roundToInt

object MeditationNotificationManager {
    private lateinit var notificationManager: NotificationManager
    private lateinit var appContext: Context

    const val CHANNEL_ID = "live_updates_channel_id"
    private const val CHANNEL_NAME = "live_updates_channel_name"
    private const val NOTIFICATION_ID = 1234

    @RequiresApi(Build.VERSION_CODES.O)
    fun initialize(context: Context, notifManager: NotificationManager) {
        appContext = context
        notificationManager = notifManager

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT).apply {
            // 👇 Quan trọng: cho phép hiển thị đầy đủ nội dung trên lock screen
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(channel)
    }

    private enum class MeditationState(
        val delay: Long,
        val title: String,
        val text: String,
        val targetProgress: Int
    ) {
        PREPARATION(5000, "Meditation: Relax", "Finding a comfortable position and clearing your mind.", 20),
        BREATHING_EXERCISES(5000, "Meditation: Breath", "Focusing on your breath, in and out.", 40),
        BODY_SCAN(5000, "Meditation: Scan", "Bringing awareness to different parts of your body.", 60),
        MINDFUL_OBSERVATION(5000, "Meditation: Observe", "Observing thoughts and feelings without judgment.", 80),
        CONCLUSION(5000, "Meditation: Finish", "Gently returning to your surroundings.", 100);

        val shortTitle: String
            get() = title.substringAfter(": ").trim()

        @RequiresApi(Build.VERSION_CODES.BAKLAVA)
        fun buildNotification(currentProgress: Int): NotificationCompat.Builder {
            return buildBaseNotification()
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(buildProgressStyle().setProgress(currentProgress))
                .setShortCriticalText(shortTitle)
                .setShowWhen(true)
                .setOngoing(true)
        }

        @RequiresApi(Build.VERSION_CODES.BAKLAVA)
        private fun buildProgressStyle(): ProgressStyle {
            val segmentColors = listOf(
                android.graphics.Color.parseColor("#FFC107"),
                android.graphics.Color.parseColor("#4CAF50"),
                android.graphics.Color.parseColor("#2196F3"),
                android.graphics.Color.parseColor("#9C27B0"),
                android.graphics.Color.parseColor("#F44336")
            )
            val pointColors = listOf(
                android.graphics.Color.parseColor("#FFEB3B"),
                android.graphics.Color.parseColor("#8BC34A"),
                android.graphics.Color.parseColor("#03A9F4"),
                android.graphics.Color.parseColor("#E91E63"),
                android.graphics.Color.parseColor("#FF5722")
            )

            return ProgressStyle()
                .setProgressPoints(
                    listOf(
                        ProgressStyle.Point(20).setColor(pointColors[0]),
                        ProgressStyle.Point(40).setColor(pointColors[1]),
                        ProgressStyle.Point(60).setColor(pointColors[2]),
                        ProgressStyle.Point(80).setColor(pointColors[3]),
                        ProgressStyle.Point(100).setColor(pointColors[4])
                    )
                )
                .setProgressSegments(
                    listOf(
                        ProgressStyle.Segment(20).setColor(segmentColors[0]),
                        ProgressStyle.Segment(20).setColor(segmentColors[1]),
                        ProgressStyle.Segment(20).setColor(segmentColors[2]),
                        ProgressStyle.Segment(20).setColor(segmentColors[3]),
                        ProgressStyle.Segment(20).setColor(segmentColors[4])
                    )
                )
                .setProgressTrackerIcon(
                    IconCompat.createWithResource(appContext, R.drawable.ic_launcher_foreground)
                )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun buildBaseNotification(): NotificationCompat.Builder {
            return NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setOngoing(true)
                .setRequestPromotedOngoing(true)
                .setShowWhen(true)
                .setShortCriticalText(shortTitle)
                // 👇 Quan trọng: Đảm bảo notification là non-sensitive
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .apply {
                    if (this@MeditationState == CONCLUSION) {
                        addAction(
                            NotificationCompat.Action.Builder(null, "End Meditation", null).build()
                        )
                    }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    fun startMeditationNotifications() {
        Logger.getLogger("MeditationNotification").log(Level.INFO, "Start meditation animation")
        animateStatesSequentially(
            states = MeditationState.entries,
            index = 0,
            lastProgress = 0
        )
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    private fun animateStatesSequentially(states: EnumEntries<MeditationState>, index: Int, lastProgress: Int) {
        if (index >= states.size) return

        val state = states[index]
        Logger.getLogger("MeditationNotification").log(Level.INFO, "Animating state: ${state.name}")

        animateProgress(state, lastProgress, state.targetProgress, state.delay) {
            animateStatesSequentially(states, index + 1, state.targetProgress)
        }
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    private fun animateProgress(
        state: MeditationState,
        startProgress: Int,
        targetProgress: Int,
        totalDuration: Long,
        onDone: () -> Unit
    ) {
        val steps = 10
        val stepDelay = totalDuration / steps
        val progressIncrement = (targetProgress - startProgress).toFloat() / steps

        for (i in 1..steps) {
            val currentProgress = (startProgress + i * progressIncrement).roundToInt()
            Handler(Looper.getMainLooper()).postDelayed({
                val notification = state.buildNotification(currentProgress).build()
                notificationManager.notify(NOTIFICATION_ID, notification)
                Logger.getLogger("MeditationNotification").log(Level.INFO, "State: ${state.name}, progress: $currentProgress")

                if (i == steps) onDone()
            }, i * stepDelay)
        }
    }

    fun stopMeditation() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
