package com.gianlucaparadise.castyourinstructions.cast

import android.util.Log
import com.gianlucaparadise.castyourinstructions.models.CastMessageResponse
import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.CastDevice
import com.google.gson.Gson

class RoutineCastingChannel(private val listener: RoutineCastingChannelListener? = null) : Cast.MessageReceivedCallback {

    private val TAG = "RoutineCastingChannel"

    val namespace = "urn:x-cast:cast-your-instructions"

    override fun onMessageReceived(castDevice: CastDevice?, namespace: String?, message: String?) {
        Log.d(TAG, "onMessageReceived: $message")
        if (message == null || listener == null) return

        val messageResponse = listener.gson.fromJson(message, CastMessageResponse::class.java)
        Log.d(TAG, "onMessageReceived parse: ${messageResponse.routine?.title}")
        listener.onMessageReceived(messageResponse)
    }

}

interface RoutineCastingChannelListener {
    val gson: Gson
    fun onMessageReceived(responseMessage: CastMessageResponse)
}