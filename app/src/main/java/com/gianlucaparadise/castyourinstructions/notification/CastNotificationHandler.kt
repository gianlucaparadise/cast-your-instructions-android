package com.gianlucaparadise.castyourinstructions.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.application.MyApplication
import android.app.PendingIntent
import android.content.Intent
import com.gianlucaparadise.castyourinstructions.MainActivity


class CastNotificationHandler : LifecycleObserver {

    private val tag = "CastNotificationHandler"

    private val channelName = "Cast your Instruction Player"
    private val channelDescription = "Player to control the casted instruction on the TV"
    private val channelId = "cast:instruction:player:channel"
    private val notificationId = 7

    val context: Context
        get() = MyApplication.instance

    init {
        createNotificationChannel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onApplicationStarted() {
        Log.d(tag, "Canceling notification")
        cancelNotification()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onApplicationStopped() {
        Log.d(tag, "Showing notification")
        showNotification()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val importance = NotificationManager.IMPORTANCE_LOW // this is to avoid notification sound or vibration
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification() {
        val intent = Intent(context, MainActivity::class.java)
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.cast_ic_notification_small_icon)

            // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            // Add media control buttons that invoke intents in your media service
            //.addAction(R.drawable.cast_ic_notification_skip_prev, "Previous", null) // #0
            .addAction(R.drawable.cast_ic_notification_pause, "Pause", null) // #1
            //.addAction(R.drawable.cast_ic_notification_skip_next, "Next", null) // #2
            .addAction(R.drawable.quantum_ic_stop_white_24, "Stop", null) // #3

            // Apply the media style template
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0 /* #1: pause button \*/)
                //.setMediaSession(mediaSession.getSessionToken())
            )

            .setContentTitle("Title")
            .setContentText("Content")
            .setContentIntent(pendingIntent) // Open app on tap
            .setOngoing(true) // this is to make notification sticky
            .setPriority(NotificationCompat.PRIORITY_LOW) // this is to avoid notification sound or vibration

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    fun cancelNotification() {
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            cancel(notificationId)
        }
    }
}