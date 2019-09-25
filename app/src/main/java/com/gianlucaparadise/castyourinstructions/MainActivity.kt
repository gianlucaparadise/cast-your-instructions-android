package com.gianlucaparadise.castyourinstructions

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.gianlucaparadise.castyourinstructions.cast.CastManager
import com.gianlucaparadise.castyourinstructions.databinding.ActivityMainBinding
import com.gianlucaparadise.castyourinstructions.fragments.RecipeDetailFragment
import com.gianlucaparadise.castyourinstructions.fragments.RecipesListFragment
import com.gianlucaparadise.castyourinstructions.fragments.RecipesListFragmentDirections
import com.gianlucaparadise.castyourinstructions.models.Recipe
import com.gianlucaparadise.castyourinstructions.viewmodels.MainViewModel
import com.gianlucaparadise.castyourinstructions.views.PlayerListener
import com.google.android.gms.cast.framework.CastButtonFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecipesListFragment.OnListFragmentInteractionListener,
    RecipeDetailFragment.OnDetailFragmentInteractionListener, PlayerListener {

    private val TAG = "Cast-your-instructions"

    private lateinit var viewModel: MainViewModel
    private lateinit var castManager: CastManager

    private var mediaRouteMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(
            this,
            NavHostFragment.findNavController(nav_host_fragment)
        )

        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.castState.observe(this,
            Observer<MainViewModel.CastStateValue> { state ->
                Log.d(TAG, "CastState changed: $state")
                when (state) {
                    MainViewModel.CastStateValue.STARTED -> invalidateOptionsMenu()
                    MainViewModel.CastStateValue.STOPPED -> {
                        // pass
                    }
                    null -> {
                        // pass
                    }
                }
            })

        binding.vm = viewModel

        castManager = CastManager(this, lifecycle, viewModel)

        player.listener = this
    }

    override fun onSupportNavigateUp() =
        NavHostFragment.findNavController(nav_host_fragment).navigateUp()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        mediaRouteMenuItem =
            CastButtonFactory.setUpMediaRouteButton(
                applicationContext,
                menu,
                R.id.media_route_menu_item
            )
        return true
    }

    override fun onCastClicked(recipe: Recipe) {
        castManager.load(recipe)
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

        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeDetailFragment(recipe)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}
