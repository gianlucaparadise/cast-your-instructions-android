package com.gianlucaparadise.castyourinstructions.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.gianlucaparadise.castyourinstructions.R
import kotlinx.android.synthetic.main.fragment_recipe_list.view.*
import kotlinx.android.synthetic.main.player_view.view.*

class Player @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.player_view, this)

        play.setOnClickListener {
            listener?.onPlayClicked()
        }

        pause.setOnClickListener {
            listener?.onPauseClicked()
        }

        stop.setOnClickListener { listener?.onStopClicked() }
    }

    var listener: PlayerListener? = null
}

interface PlayerListener {
    fun onPlayClicked()
    fun onPauseClicked()
    fun onStopClicked()
}