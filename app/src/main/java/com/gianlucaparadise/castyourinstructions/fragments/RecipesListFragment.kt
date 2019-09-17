package com.gianlucaparadise.castyourinstructions.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.adapters.MyRecipeRecyclerViewAdapter
import com.gianlucaparadise.castyourinstructions.models.Recipe
import com.gianlucaparadise.castyourinstructions.models.Recipes
import com.gianlucaparadise.castyourinstructions.viewmodels.RecipesListViewModel

/**
 * A fragment representing a list of Recipes.
 */
class RecipesListFragment : Fragment() {

    private lateinit var viewModel: RecipesListViewModel
    private var listener: OnListFragmentInteractionListener? = null

    private var recipesAdapter: MyRecipeRecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the adapter
        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(context)

            recipesAdapter = MyRecipeRecyclerViewAdapter(listener)
            view.adapter = recipesAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(activity!!)[RecipesListViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: RecipesListViewModel) {
        // Update the list when the data changes
        viewModel.recipesObservable.observe(this,
            Observer<Recipes> { recipes ->
                if (recipes != null) {
                    recipesAdapter?.recipes = recipes
                }
            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(recipe: Recipe?)
    }
}
