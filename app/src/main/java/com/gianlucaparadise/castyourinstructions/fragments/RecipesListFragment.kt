package com.gianlucaparadise.castyourinstructions.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.adapters.MyRecipeRecyclerViewAdapter
import com.gianlucaparadise.castyourinstructions.models.Recipe
import com.gianlucaparadise.castyourinstructions.models.Recipes
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [RecipesListFragment.OnListFragmentInteractionListener] interface.
 */
class RecipesListFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val recipes = getRecipes()

            if (recipes != null) {
                view.layoutManager = LinearLayoutManager(context)
                view.adapter =
                    MyRecipeRecyclerViewAdapter(recipes, listener)
            }
        }
        return view
    }

    private fun getRecipes(): Recipes? {

        try {
            val raw = resources.openRawResource(R.raw.recipes)
            val reader = BufferedReader(InputStreamReader(raw))
            val gson = Gson()
            val recipes = gson.fromJson(reader, Recipes::class.java)

            return recipes

        } catch (ex: Exception) {
            return null
        }
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
        fun onListFragmentInteraction(item: Recipe?)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            RecipesListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
