package com.gianlucaparadise.castyourinstructions.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.databinding.FragmentInstructionBinding

import com.gianlucaparadise.castyourinstructions.models.Instruction

import kotlinx.android.synthetic.main.fragment_instruction.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyInstructionRecyclerViewAdapter : RecyclerView.Adapter<MyInstructionRecyclerViewAdapter.ViewHolder>() {

    var instructions: List<Instruction>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: FragmentInstructionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_instruction, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(instructions?.get(position), position)

    override fun getItemCount(): Int = instructions?.size ?: 0

    inner class ViewHolder(val binding: FragmentInstructionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Instruction?, position: Int) {
            binding.position = position.toString()
            binding.item = item
            binding.handlers = this@MyInstructionRecyclerViewAdapter
            binding.executePendingBindings()
        }

        override fun toString(): String {
            return super.toString() + " '" + binding.root.content.text + "'"
        }
    }
}
