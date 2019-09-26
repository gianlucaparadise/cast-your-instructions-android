package com.gianlucaparadise.castyourinstructions.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.adapters.MyInstructionRecyclerViewAdapter
import com.gianlucaparadise.castyourinstructions.databinding.FragmentInstructionListBinding
import com.gianlucaparadise.castyourinstructions.models.Routine
import com.gianlucaparadise.castyourinstructions.viewmodels.RoutineDetailViewModel
import kotlinx.android.synthetic.main.fragment_instruction_list.*

/**
 * A fragment representing the detail of a routine: a list of Instructions.
 */
class RoutineDetailFragment : Fragment() {

    val arguments: RoutineDetailFragmentArgs by navArgs()
    private lateinit var viewModel: RoutineDetailViewModel
    private var listener: OnDetailFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = activity?.run {
            ViewModelProviders.of(activity!!)[RoutineDetailViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val routine = arguments.routine
        viewModel.routine = routine

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

    private fun observeViewModel(viewModel: RoutineDetailViewModel) {
        val routine= viewModel.routine ?: return

        val adapter = this.list.adapter as? MyInstructionRecyclerViewAdapter
        adapter?.instructions = routine.instructions
    }

    fun onSendClicked(view: View) {
        Log.d("Send", "CLICKED")
        val routine = viewModel.routine ?: return
        listener?.onCastClicked(routine)
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
        fun onCastClicked(routine: Routine)
    }
}
