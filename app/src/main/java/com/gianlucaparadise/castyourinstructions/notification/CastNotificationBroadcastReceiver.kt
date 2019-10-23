package com.gianlucaparadise.castyourinstructions.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.gianlucaparadise.castyourinstructions.application.MyApplication

class CastNotificationBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "CastNotificationBrdcst"

        const val ACTION_PLAY = "com.gianlucaparadise.castyourinstructions.notification.CastNotificationBroadcastReceiver.PLAY"
        const val ACTION_PAUSE = "com.gianlucaparadise.castyourinstructions.notification.CastNotificationBroadcastReceiver.PAUSE"
        const val ACTION_STOP = "com.gianlucaparadise.castyourinstructions.notification.CastNotificationBroadcastReceiver.STOP"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive")
        if (context == null || intent == null) return

        // TODO: remove the following line to avoid reference to application for better decoupling
        val castManager =  MyApplication.instance.castManager

        when (intent.action) {
            ACTION_STOP -> {
                castManager.stop()
            }
            ACTION_PAUSE -> {
                castManager.pause()
            }
            ACTION_PLAY -> {
                castManager.play()
            }
            else -> {
                Log.d(TAG, "Action not handled: ${intent.action}")
            }
        }
    }
}