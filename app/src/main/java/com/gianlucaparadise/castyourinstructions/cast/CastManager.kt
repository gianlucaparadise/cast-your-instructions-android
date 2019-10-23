package com.gianlucaparadise.castyourinstructions.cast

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.gianlucaparadise.castyourinstructions.models.*
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.gson.Gson

class CastManager(context: Context) : LifecycleObserver, RoutineCastingChannelListener {

    companion object {
        const val TAG = "CastManager"
    }

    private val context: Context = context.applicationContext

    override val gson: Gson by lazy { Gson() }

    private var mCastContext: CastContext? = null

    private val myChannel = RoutineCastingChannel(this)
    private var mCastSession: CastSession? = null
        set(value) {
            field = value

            try {
                field?.setMessageReceivedCallbacks(myChannel.namespace, myChannel)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "Exception while creating channel", e)
            }
        }

    private lateinit var mSessionManager: SessionManager
    private val mSessionManagerListener = CastSessionManagerListener()

    val castConnectionState: MutableLiveData<CastConnectionState> = MutableLiveData()
    val castPlayerState: MutableLiveData<CastPlayerState> = MutableLiveData()
    val routine: MutableLiveData<Routine> = MutableLiveData()
    val lastSelectedInstruction: MutableLiveData<Instruction?> = MutableLiveData()

    private val listeners = mutableListOf<CastManagerListener>()
    fun addListener(listener: CastManagerListener) = listeners.add(listener)
    fun removeListener(listener: CastManagerListener) = listeners.remove(listener)

    init {
        Log.d(TAG, "Constructed")
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun load(routine: Routine) {
        val message = CastMessage(MessageType.LOAD, routine)
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
                Log.d(TAG, "No session")
                return
            }

            val messageString = gson.toJson(message)
            castSession.sendMessage(myChannel.namespace, messageString)

        } catch (ex: Exception) {
            Log.e(TAG, "Error while sending ${message.type}:")
            Log.e(TAG, ex.toString())
        }
    }

    override fun onMessageReceived(responseMessage: CastMessageResponse) {
        if (!listeners.any()) return

        val routine = responseMessage.routine
        this.routine.value = responseMessage.routine

        when (responseMessage.type) {
            ResponseMessageType.LOADED -> {
                castPlayerState.value = CastPlayerState.LOADED
                lastSelectedInstruction.value = null
                listeners.forEach { it.onLoaded(routine) }
            }
            ResponseMessageType.PLAYED -> {
                castPlayerState.value = CastPlayerState.PLAYING
                listeners.forEach { it.onPlayed(routine) }
            }
            ResponseMessageType.PAUSED -> {
                castPlayerState.value = CastPlayerState.PAUSED
                listeners.forEach { it.onPaused(routine) }
            }
            ResponseMessageType.STOPPED -> {
                castPlayerState.value = CastPlayerState.STOPPED
                lastSelectedInstruction.value = null
                listeners.forEach { it.onStopped(routine) }
            }
            ResponseMessageType.SELECTED_INSTRUCTION -> listeners.forEach {
                val selectedInstructionIndex = responseMessage.selectedInstructionIndex
                lastSelectedInstruction.value =
                    routine?.instructions?.getOrNull(selectedInstructionIndex ?: -1)
                it.onSelectedInstruction(routine, selectedInstructionIndex)
            }
            else -> {
                Log.d(TAG, "State not handled: ${responseMessage.type}")
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "Activity: ON_CREATE")
        mCastContext = CastContext.getSharedInstance(context)
        mSessionManager = CastContext.getSharedInstance(context).sessionManager
        mCastSession = mCastContext!!.sessionManager.currentCastSession
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(TAG, "Activity: ON_RESUME")

        // Intent with format: "<your_app_scheme>://<your_app_host><your_app_path>"
        // val intentToJoinUri = Uri.parse("https://cast-your-instructions.surge.sh/cast/join")
        // if (mainActivity.intent?.data?.equals(intentToJoinUri) == true) {
        //     mSessionManager.startSession(mainActivity.intent)
        // }

        mCastSession = mSessionManager.currentCastSession
        mSessionManager.addSessionManagerListener(mSessionManagerListener, CastSession::class.java)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.d(TAG, "Activity: ON_PAUSE")
        mSessionManager.removeSessionManagerListener(
            mSessionManagerListener,
            CastSession::class.java
        )
        // I can't set this to null because I need the cast session to send commands from notification
        // mCastSession = null
    }

    private inner class CastSessionManagerListener : SessionManagerListener<CastSession> {
        private var TAG = "SessionManagerListenerImpl"

        override fun onSessionSuspended(session: CastSession?, p1: Int) {
            Log.d(TAG, "onSessionSuspended")
        }

        override fun onSessionStarting(session: CastSession?) {
            Log.d(TAG, "onSessionStarting")
            mCastSession = session
        }

        override fun onSessionResuming(session: CastSession?, p1: String?) {
            Log.d(TAG, "onSessionResuming")
            mCastSession = session
        }

        override fun onSessionEnding(session: CastSession?) {
            Log.d(TAG, "onSessionEnding")
        }

        override fun onSessionStartFailed(session: CastSession?, p1: Int) {
            Log.d(TAG, "onSessionStartFailed")
            castConnectionState.value = CastConnectionState.NOT_CONNECTED
            listeners.forEach { it.onCastStopped() }
        }

        override fun onSessionResumeFailed(session: CastSession?, p1: Int) {
            Log.d(TAG, "onSessionResumeFailed")
            castConnectionState.value = CastConnectionState.NOT_CONNECTED
            listeners.forEach { it.onCastStopped() }
        }

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            Log.d(TAG, "onSessionStarted")
            mCastSession = session
            castConnectionState.value = CastConnectionState.CONNECTED
            listeners.forEach { it.onCastStarted() }
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            Log.d(TAG, "onSessionResumed")
            mCastSession = session
            castConnectionState.value = CastConnectionState.CONNECTED
            listeners.forEach { it.onCastStarted() }
        }

        override fun onSessionEnded(session: CastSession, error: Int) {
            Log.d(TAG, "onSessionEnded")
            castConnectionState.value = CastConnectionState.NOT_CONNECTED
            listeners.forEach { it.onCastStopped() }
        }
    }

    enum class CastConnectionState {
        NOT_CONNECTED,
        CONNECTED,
    }

    enum class CastPlayerState {
        UNLOADED,
        LOADED,
        STOPPED,
        PLAYING,
        PAUSED,
    }

    interface CastManagerListener {
        fun onCastStarted() {}
        fun onCastStopped() {}

        fun onLoaded(routine: Routine?) {}
        fun onPlayed(routine: Routine?) {}
        fun onPaused(routine: Routine?) {}
        fun onStopped(routine: Routine?) {}
        fun onSelectedInstruction(routine: Routine?, selectedInstructionIndex: Int?) {}
    }
}