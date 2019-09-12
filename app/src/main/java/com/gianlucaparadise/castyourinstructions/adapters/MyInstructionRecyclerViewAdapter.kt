package com.gianlucaparadise.castyourinstructions.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gianlucaparadise.castyourinstructions.R

import com.gianlucaparadise.castyourinstructions.models.Instruction

import kotlinx.android.synthetic.main.fragment_instruction.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyInstructionRecyclerViewAdapter(
    private val mValues: List<Instruction>
) : RecyclerView.Adapter<MyInstructionRecyclerViewAdapter.ViewHolder>() {

    // private val mOnClickListener: View.OnClickListener

    init {
        // mOnClickListener = View.OnClickListener { v ->
        //     val item = v.tag as Instruction
        //     // Clicked
        // }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_instruction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = position.toString()
        holder.mContentView.text = item.name

        with(holder.mView) {
            tag = item
            // setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
