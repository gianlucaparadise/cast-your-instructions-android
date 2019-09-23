package com.gianlucaparadise.castyourinstructions.cast

import android.util.Log
import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.CastDevice

class MyChannel : Cast.MessageReceivedCallback {

    val namespace = "urn:x-cast:cast-your-instructions"

    override fun onMessageReceived(castDevice: CastDevice?, namespace: String?, message: String?) {
        Log.d("MyChannel", "onMessageReceived: " + message);
    }

}