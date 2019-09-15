package com.gianlucaparadise.castyourinstructions.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.castyourinstructions.databinding.FragmentRecipeBinding
import com.gianlucaparadise.castyourinstructions.fragments.RecipesListFragment.OnListFragmentInteractionListener
import com.gianlucaparadise.castyourinstructions.models.Recipe
import kotlinx.android.synthetic.main.fragment_recipe.view.*

/**
 * [RecyclerView.Adapter] that can display a [Recipe] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyRecipeRecyclerViewAdapter// Notify the active callbacks interface (the activity, if the fragment is attached to
// one) that an item has been selected.
    (private val mListener: OnListFragmentInteractionListener?) :
    RecyclerView.Adapter<MyRecipeRecyclerViewAdapter.ViewHolder>() {

    var recipes: List<Recipe>? = null
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentRecipeBinding.inflate(inflater, parent, false)
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_recipe, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(recipes?.get(position))

    override fun getItemCount(): Int = recipes?.size ?: 0

    fun onRecipeClicked(v: View) {
        val item = v.tag as Recipe
        // Notify the active callbacks interface (the activity, if the fragment is attached to
        // one) that an item has been selected.
        mListener?.onListFragmentInteraction(item)
    }

    inner class ViewHolder(val binding: FragmentRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe?) {
            binding.item = item
            binding.handlers = this@MyRecipeRecyclerViewAdapter
            binding.executePendingBindings()
        }

        override fun toString(): String {
            return super.toString() + " '" + binding.root.content.text + "'"
        }
    }
}
