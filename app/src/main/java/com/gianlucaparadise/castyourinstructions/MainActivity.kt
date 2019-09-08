package com.gianlucaparadise.castyourinstructions

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.cast.framework.*

class MainActivity : AppCompatActivity() {

    private var mCastContext: CastContext? = null
    private var mediaRouteMenuItem: MenuItem? = null

    private var mCastSession: CastSession? = null
    private lateinit var mSessionManager: SessionManager
    private val mSessionManagerListener = SessionManagerListenerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCastContext = CastContext.getSharedInstance(this)
        mSessionManager = CastContext.getSharedInstance(this).sessionManager
        mCastSession = mCastContext!!.sessionManager.currentCastSession
    }

    override fun onResume() {
        mCastSession = mSessionManager.currentCastSession
        mSessionManager.addSessionManagerListener(mSessionManagerListener, CastSession::class.java)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mSessionManager.removeSessionManagerListener(mSessionManagerListener, CastSession::class.java)
        mCastSession = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        mediaRouteMenuItem =
            CastButtonFactory.setUpMediaRouteButton(applicationContext, menu, R.id.media_route_menu_item)
        return true
    }

    private inner class SessionManagerListenerImpl : SessionManagerListener<CastSession> {
        private var tag = "SessionManagerListenerImpl"

        override fun onSessionSuspended(p0: CastSession?, p1: Int) {
            Log.d(tag, "onSessionSuspended")
        }

        override fun onSessionStarting(p0: CastSession?) {
            Log.d(tag, "onSessionStarting")
        }

        override fun onSessionResuming(p0: CastSession?, p1: String?) {
            Log.d(tag, "onSessionResuming")
        }

        override fun onSessionEnding(p0: CastSession?) {
            Log.d(tag, "onSessionEnding")
        }

        override fun onSessionStartFailed(p0: CastSession?, p1: Int) {
            Log.d(tag, "onSessionStartFailed")
        }

        override fun onSessionResumeFailed(p0: CastSession?, p1: Int) {
            Log.d(tag, "onSessionResumeFailed")
        }

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            Log.d(tag, "onSessionStarted")
            mCastSession = session
            invalidateOptionsMenu()
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            Log.d(tag, "onSessionResumed")
            invalidateOptionsMenu()
        }

        override fun onSessionEnded(session: CastSession, error: Int) {
            Log.d(tag, "onSessionEnded")
            finish()
        }
    }
}
