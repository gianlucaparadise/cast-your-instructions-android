package com.gianlucaparadise.castyourinstructions.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.fragments.RecipesListFragment.OnListFragmentInteractionListener
import com.gianlucaparadise.castyourinstructions.models.Recipe
import kotlinx.android.synthetic.main.fragment_recipe.view.*

/**
 * [RecyclerView.Adapter] that can display a [Recipe] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyRecipeRecyclerViewAdapter(
    private val mValues: List<Recipe>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyRecipeRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Recipe
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.title

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
