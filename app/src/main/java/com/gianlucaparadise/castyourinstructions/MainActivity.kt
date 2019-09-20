package com.gianlucaparadise.castyourinstructions


class MainActivity : AppCompatActivity(), RecipesListFragment.OnListFragmentInteractionListener,
    RecipeDetailFragment.OnDetailFragmentInteractionListener {

    private val TAG = "Cast-your-instructions"

    private var mCastContext: CastContext? = null
    private var mediaRouteMenuItem: MenuItem? = null

    private var mCastSession: CastSession? = null
    private lateinit var mSessionManager: SessionManager
    private val mSessionManagerListener = SessionManagerListenerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, NavHostFragment.findNavController(nav_host_fragment))

        mCastContext = CastContext.getSharedInstance(this)
        mSessionManager = CastContext.getSharedInstance(this).sessionManager
        mCastSession = mCastContext!!.sessionManager.currentCastSession
    }

    override fun onSupportNavigateUp() = NavHostFragment.findNavController(nav_host_fragment).navigateUp()

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
            player.visibility = View.GONE
        }

        override fun onSessionResumeFailed(p0: CastSession?, p1: Int) {
            Log.d(tag, "onSessionResumeFailed")
            player.visibility = View.GONE
        }

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            Log.d(tag, "onSessionStarted")
            mCastSession = session
            invalidateOptionsMenu()
            player.visibility = View.VISIBLE
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            Log.d(tag, "onSessionResumed")
            invalidateOptionsMenu()
            player.visibility = View.VISIBLE
        }

        override fun onSessionEnded(session: CastSession, error: Int) {
            Log.d(tag, "onSessionEnded")
            player.visibility = View.GONE
        }
    }

    override fun onCastClicked(recipe: Recipe) {
        try {
            val castSession = mCastSession
            if (castSession == null) {
                Log.d("MainActivity", "No session")
                return
            }

            val gson = Gson()
            val recipeString = gson.toJson(recipe)
            castSession.sendMessage("urn:x-cast:cast-your-instructions", recipeString)

        } catch (ex: Exception) {
            Log.e(TAG, "Error while casting:")
            Log.e(TAG, ex.toString())
        }
    }

    override fun onListFragmentInteraction(recipe: Recipe?) {
        Log.d(TAG, "Recipe clicked $recipe")
        if (recipe == null) return

        val action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeDetailFragment(recipe)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}
