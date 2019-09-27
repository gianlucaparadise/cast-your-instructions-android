package com.gianlucaparadise.castyourinstructions.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.databinding.FragmentRoutineBinding
import com.gianlucaparadise.castyourinstructions.fragments.RoutinesListFragment.OnListFragmentInteractionListener
import com.gianlucaparadise.castyourinstructions.models.Routine
import kotlinx.android.synthetic.main.fragment_routine.view.*

/**
 * [RecyclerView.Adapter] that can display a [Routine] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyRoutineRecyclerViewAdapter// Notify the active callbacks interface (the activity, if the fragment is attached to
// one) that an item has been selected.
    (private val mListener: OnListFragmentInteractionListener?) :
    RecyclerView.Adapter<MyRoutineRecyclerViewAdapter.ViewHolder>() {

    var routines: List<Routine>? = null
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: FragmentRoutineBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_routine, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(routines?.get(position))

    override fun getItemCount(): Int = routines?.size ?: 0

    fun onRoutineClicked(v: View) {
        val item = v.tag as Routine
        // Notify the active callbacks interface (the activity, if the fragment is attached to
        // one) that an item has been selected.
        mListener?.onListFragmentInteraction(item)
    }

    inner class ViewHolder(val binding: FragmentRoutineBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Routine?) {
            binding.item = item
            binding.handlers = this@MyRoutineRecyclerViewAdapter
            binding.executePendingBindings()
        }

        override fun toString(): String {
            return super.toString() + " '" + binding.root.content.text + "'"
        }
    }
}
