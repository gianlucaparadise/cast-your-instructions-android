package com.gianlucaparadise.castyourinstructions.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.adapters.MyInstructionRecyclerViewAdapter

import com.gianlucaparadise.castyourinstructions.models.Recipe
import kotlinx.android.synthetic.main.fragment_instruction_list.*

/**
 * A fragment representing the detail of a recipe: a list of Instructions.
 */
class RecipeDetailFragment : Fragment() {

    private lateinit var recipe: Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            recipe = it.getSerializable(ARG_RECIPE) as Recipe
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instruction_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.detail_title.text = recipe.title

        val instructions = recipe.instructions;
        // Set the adapter
        if (instructions != null) {
            this.list.layoutManager = LinearLayoutManager(context)
            this.list.adapter = MyInstructionRecyclerViewAdapter(instructions)
        }
    }

    companion object {

        const val ARG_RECIPE = "recipe"

        @JvmStatic
        fun newInstance(recipe: Recipe) =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_RECIPE, recipe)
                }
            }
    }
}
