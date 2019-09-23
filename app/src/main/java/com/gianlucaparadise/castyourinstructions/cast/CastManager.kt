package com.gianlucaparadise.castyourinstructions.cast

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.gianlucaparadise.castyourinstructions.MainActivity
import com.gianlucaparadise.castyourinstructions.models.CastMessage
import com.gianlucaparadise.castyourinstructions.models.MessageType
import com.gianlucaparadise.castyourinstructions.models.Recipe
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.gson.Gson

class CastManager(
    val mainActivity: MainActivity,
    val lifecycle: Lifecycle,
    var listener: CastManagerListener? = null
) : LifecycleObserver {
    private val TAG = "CastManager"

    private val gson : Gson by lazy { Gson() }

    private var mCastContext: CastContext? = null

    private val myChannel = MyChannel()
    private var mCastSession: CastSession? = null
        set(value) {
            field = value

            try {
                field?.setMessageReceivedCallbacks(myChannel.namespace, myChannel)
            }
            catch (e: java.lang.Exception) {
                Log.e(TAG, "Exception while creating channel", e)
            }
        }

    private lateinit var mSessionManager: SessionManager
    private val mSessionManagerListener = CastSessionManagerListener()

    init {
        Log.d(TAG, "Constructed")
        lifecycle.addObserver(this)
    }

    fun load(recipe: Recipe) {
        val message = CastMessage(MessageType.LOAD, recipe)
        sendMessage(message)
    }

    fun play() {
        val message = CastMessage(MessageType.PLAY)
        sendMessage(message)
    }

    fun pause() {
        val message = CastMessage(MessageType.PAUSE)
        sendMessage(message)
    }

    fun stop() {
        val message = CastMessage(MessageType.STOP)
        sendMessage(message)
    }

    private fun sendMessage(message: CastMessage) {
        try {
            val castSession = mCastSession
            if (castSession == null) {
                Log.d("MainActivity", "No session")
                return
            }

            val messageString = gson.toJson(message)
            castSession.sendMessage(myChannel.namespace, messageString)

        } catch (ex: Exception) {
            Log.e(TAG, "Error while sending ${message.type}:")
            Log.e(TAG, ex.toString())
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "Activity: ON_CREATE")
        mCastContext = CastContext.getSharedInstance(mainActivity)
        mSessionManager = CastContext.getSharedInstance(mainActivity).sessionManager
        mCastSession = mCastContext!!.sessionManager.currentCastSession
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(TAG, "Activity: ON_RESUME")
        mCastSession = mSessionManager.currentCastSession
        mSessionManager.addSessionManagerListener(mSessionManagerListener, CastSession::class.java)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.d(TAG, "Activity: ON_PAUSE")
        mSessionManager.removeSessionManagerListener(mSessionManagerListener, CastSession::class.java)
        mCastSession = null
    }

    private inner class CastSessionManagerListener : SessionManagerListener<CastSession> {
        private var TAG = "SessionManagerListenerImpl"

        override fun onSessionSuspended(p0: CastSession?, p1: Int) {
            Log.d(TAG, "onSessionSuspended")
        }

        override fun onSessionStarting(p0: CastSession?) {
            Log.d(TAG, "onSessionStarting")
        }

        override fun onSessionResuming(p0: CastSession?, p1: String?) {
            Log.d(TAG, "onSessionResuming")
        }

        override fun onSessionEnding(p0: CastSession?) {
            Log.d(TAG, "onSessionEnding")
        }

        override fun onSessionStartFailed(p0: CastSession?, p1: Int) {
            Log.d(TAG, "onSessionStartFailed")
            listener?.onCastStopped()
        }

        override fun onSessionResumeFailed(p0: CastSession?, p1: Int) {
            Log.d(TAG, "onSessionResumeFailed")
            listener?.onCastStopped()
        }

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            Log.d(TAG, "onSessionStarted")
            mCastSession = session
            listener?.onCastStarted()
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            Log.d(TAG, "onSessionResumed")
            listener?.onCastStarted()
        }

        override fun onSessionEnded(session: CastSession, error: Int) {
            Log.d(TAG, "onSessionEnded")
            listener?.onCastStopped()
        }
    }

    interface CastManagerListener {
        fun onCastStarted();
        fun onCastStopped();
    }
}