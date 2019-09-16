package com.gianlucaparadise.castyourinstructions.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.adapters.MyInstructionRecyclerViewAdapter
import com.gianlucaparadise.castyourinstructions.databinding.FragmentInstructionListBinding
import com.gianlucaparadise.castyourinstructions.models.Recipe
import com.gianlucaparadise.castyourinstructions.viewmodels.RecipeDetailViewModel
import kotlinx.android.synthetic.main.fragment_instruction_list.*

/**
 * A fragment representing the detail of a recipe: a list of Instructions.
 */
class RecipeDetailFragment : Fragment() {

    private lateinit var viewModel: RecipeDetailViewModel
    private var listener: OnDetailFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recipe = arguments?.let {
            it.getSerializable(ARG_RECIPE) as Recipe
        }

        val viewModelFactory = RecipeDetailViewModel.RecipeDetailViewModelFactory(recipe)
        viewModel = activity?.run {
            ViewModelProviders.of(activity!!, viewModelFactory)[RecipeDetailViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val binding: FragmentInstructionListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_instruction_list, container, false)
        val view : View  = binding.root

        binding.lifecycleOwner = this
        binding.vm = viewModel
        binding.handlers = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.list.layoutManager = LinearLayoutManager(context)
        this.list.adapter = MyInstructionRecyclerViewAdapter()

        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: RecipeDetailViewModel) {
        val recipe= viewModel.recipe ?: return

        val adapter = this.list.adapter as? MyInstructionRecyclerViewAdapter
        adapter?.instructions = recipe.instructions
    }

    fun onSendClicked(view: View) {
        Log.d("Send", "CLICKED")
        val recipe = viewModel.recipe ?: return
        listener?.onCastClicked(recipe)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDetailFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnDetailFragmentInteractionListener {
        fun onCastClicked(recipe: Recipe)
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
