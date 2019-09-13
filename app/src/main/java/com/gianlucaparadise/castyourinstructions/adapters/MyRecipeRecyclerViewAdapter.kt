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
class MyRecipeRecyclerViewAdapter// Notify the active callbacks interface (the activity, if the fragment is attached to
// one) that an item has been selected.
    (private val mListener: OnListFragmentInteractionListener?) :
    RecyclerView.Adapter<MyRecipeRecyclerViewAdapter.ViewHolder>() {

    var recipes: List<Recipe>? = null
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }

    private val mOnClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (recipes == null) return

        val item = recipes!![position]
        holder.mContentView.text = item.title

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = recipes?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Recipe
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }
}
