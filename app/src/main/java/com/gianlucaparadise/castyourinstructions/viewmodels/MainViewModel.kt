package com.gianlucaparadise.castyourinstructions.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gianlucaparadise.castyourinstructions.cast.CastManager
import com.gianlucaparadise.castyourinstructions.models.Routine

class MainViewModel : ViewModel(), CastManager.CastManagerListener {

    private val TAG = "MainViewModel"

    val routineTitle: MutableLiveData<String> = MutableLiveData()
    val currentInstructionTitle: MutableLiveData<String> = MutableLiveData()

    val castState: MutableLiveData<CastStateValue> = MutableLiveData()

    val isPlayerVisible: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * This is true when PlayButton is visible and is false when PauseButton is visible
     */
    val canPlay: MutableLiveData<Boolean> = MutableLiveData()

    init {
        routineTitle.value = ""
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

    override fun onLoaded(routine: Routine?) {
        super.onLoaded(routine)
        routineTitle.value = routine?.title
        canPlay.value = true
        currentInstructionTitle.value = null
    }

    override fun onPaused(routine: Routine?) {
        super.onPaused(routine)
        canPlay.value = true
    }

    override fun onPlayed(routine: Routine?) {
        super.onPlayed(routine)
        canPlay.value = false
    }

    override fun onStopped(routine: Routine?) {
        super.onStopped(routine)
        canPlay.value = true
        currentInstructionTitle.value = null
    }

    override fun onSelectedInstruction(routine: Routine?, selectedInstructionIndex: Int?) {
        super.onSelectedInstruction(routine, selectedInstructionIndex)
        Log.d(TAG, "Selected instruction: $selectedInstructionIndex")

        val instructions = routine?.instructions ?: return
        if (selectedInstructionIndex == null || selectedInstructionIndex >= instructions.size) return

        currentInstructionTitle.value = instructions[selectedInstructionIndex].name
    }

    enum class CastStateValue {
        STARTED,
        STOPPED,
    }
}