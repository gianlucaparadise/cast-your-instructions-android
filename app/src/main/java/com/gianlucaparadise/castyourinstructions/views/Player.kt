package com.gianlucaparadise.castyourinstructions.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.gianlucaparadise.castyourinstructions.R
import kotlinx.android.synthetic.main.player_view.view.*

class Player @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var listener: PlayerListener? = null

    var title: String?
        get() {
            return txtTitle.text.toString()
        }
        set(value) {
            txtTitle.text = value
        }

    var titleTextColor: Int
        get() {
            return txtTitle.currentTextColor
        }
        set(value) {
            txtTitle.setTextColor(value)
        }

    var playPauseState: PlayPauseStateValue
        get() {
            return if (play.visibility == View.VISIBLE) PlayPauseStateValue.Play else PlayPauseStateValue.Pause
        }
        set(value) {
            play.visibility =
                if (value == PlayPauseStateValue.Play) View.VISIBLE else View.INVISIBLE
            pause.visibility =
                if (value == PlayPauseStateValue.Pause) View.VISIBLE else View.INVISIBLE
        }

    init {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.player_view, this)

        titleTextColor = Color.WHITE
        playPauseState = PlayPauseStateValue.Play

        attrs?.run {

            val typedArray: TypedArray = context
                .obtainStyledAttributes(attrs, R.styleable.Player)

            try {
                if (typedArray.hasValue(R.styleable.Player_title)) {
                    title = typedArray
                        .getString(R.styleable.Player_title) ?: ""
                }

                if (typedArray.hasValue(R.styleable.Player_titleTextColor)) {
                    titleTextColor = typedArray
                        .getColor(R.styleable.Player_titleTextColor, Color.WHITE)
                }

                if (typedArray.hasValue(R.styleable.Player_playPauseState)) {
                    val playPauseStateRaw = typedArray
                        .getInteger(R.styleable.Player_playPauseState, 0)

                    playPauseState = PlayPauseStateValue.fromInt(playPauseStateRaw)
                }
            } finally {
                typedArray.recycle()
            }
        }

        play.setOnClickListener {
            listener?.onPlayClicked()
        }

        pause.setOnClickListener {
            listener?.onPauseClicked()
        }

        stop.setOnClickListener { listener?.onStopClicked() }
    }

    enum class PlayPauseStateValue(val num: Int) {
        Play(0),
        Pause(1);

        companion object {
            fun fromInt(value: Int) = PlayPauseStateValue.values().first { it.num == value }
        }
    }
}

interface PlayerListener {
    fun onPlayClicked()
    fun onPauseClicked()
    fun onStopClicked()
}