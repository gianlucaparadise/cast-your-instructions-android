package com.gianlucaparadise.castyourinstructions.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gianlucaparadise.castyourinstructions.cast.CastManager
import com.gianlucaparadise.castyourinstructions.models.Recipe

class MainViewModel : ViewModel(), CastManager.CastManagerListener {

    private val TAG = "MainViewModel"

    val recipeTitle: MutableLiveData<String> = MutableLiveData()
    val currentInstructionTitle: MutableLiveData<String> = MutableLiveData()

    val castState: MutableLiveData<CastStateValue> = MutableLiveData()

    val isPlayerVisible: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * This is true when PlayButton is visible and is false when PauseButton is visible
     */
    val canPlay: MutableLiveData<Boolean> = MutableLiveData()

    init {
        recipeTitle.value = ""
        castState.value = CastStateValue.STOPPED
        isPlayerVisible.value = false
        canPlay.value = true
    }

    override fun onCastStarted() {
        super.onCastStarted()
        Log.d(TAG, "castStarted")
        castState.value = CastStateValue.STARTED
        isPlayerVisible.value = true
        canPlay.value = true
    }

    override fun onCastStopped() {
        super.onCastStarted()
        castState.value = CastStateValue.STOPPED
        isPlayerVisible.value = false
        canPlay.value = true
        currentInstructionTitle.value = null
    }

    override fun onLoaded(recipe: Recipe?) {
        super.onLoaded(recipe)
        recipeTitle.value = recipe?.title
        canPlay.value = true
        currentInstructionTitle.value = null
    }

    override fun onPaused(recipe: Recipe?) {
        super.onPaused(recipe)
        canPlay.value = true
    }

    override fun onPlayed(recipe: Recipe?) {
        super.onPlayed(recipe)
        canPlay.value = false
    }

    override fun onStopped(recipe: Recipe?) {
        super.onStopped(recipe)
        canPlay.value = true
        currentInstructionTitle.value = null
    }

    override fun onSelectedInstruction(recipe: Recipe?, selectedInstructionIndex: Int?) {
        super.onSelectedInstruction(recipe, selectedInstructionIndex)
        Log.d(TAG, "Selected instruction: $selectedInstructionIndex")

        val instructions = recipe?.instructions ?: return
        if (selectedInstructionIndex == null || selectedInstructionIndex >= instructions.size) return

        currentInstructionTitle.value = instructions[selectedInstructionIndex].name
    }

    enum class CastStateValue {
        STARTED,
        STOPPED,
    }
}