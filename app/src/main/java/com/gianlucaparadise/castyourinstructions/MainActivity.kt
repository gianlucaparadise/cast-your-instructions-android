package com.gianlucaparadise.castyourinstructions

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.gianlucaparadise.castyourinstructions.cast.CastManager
import com.gianlucaparadise.castyourinstructions.fragments.RecipeDetailFragment
import com.gianlucaparadise.castyourinstructions.fragments.RecipesListFragment
import com.gianlucaparadise.castyourinstructions.fragments.RecipesListFragmentDirections
import com.gianlucaparadise.castyourinstructions.models.Recipe
import com.gianlucaparadise.castyourinstructions.views.PlayerListener
import com.google.android.gms.cast.framework.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecipesListFragment.OnListFragmentInteractionListener,
    RecipeDetailFragment.OnDetailFragmentInteractionListener, CastManager.CastManagerListener, PlayerListener {

    private val TAG = "Cast-your-instructions"

    private val castManager: CastManager = CastManager(this, lifecycle, this)

    private var mediaRouteMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, NavHostFragment.findNavController(nav_host_fragment))

        player.listener = this
    }

    override fun onSupportNavigateUp() = NavHostFragment.findNavController(nav_host_fragment).navigateUp()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        mediaRouteMenuItem =
            CastButtonFactory.setUpMediaRouteButton(applicationContext, menu, R.id.media_route_menu_item)
        return true
    }

    override fun onCastClicked(recipe: Recipe) {
        castManager.load(recipe)
    }

    override fun onCastStarted() {
        invalidateOptionsMenu()
        player.visibility = View.VISIBLE
    }

    override fun onCastStopped() {
        player.visibility = View.GONE
    }

    override fun onPlayClicked() {
        castManager.play()
    }

    override fun onPauseClicked() {
        castManager.pause()
    }

    override fun onStopClicked() {
        castManager.stop()
    }

    override fun onListFragmentInteraction(recipe: Recipe?) {
        Log.d(TAG, "Recipe clicked $recipe")
        if (recipe == null) return

        val action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeDetailFragment(recipe)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}
