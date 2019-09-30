package com.gianlucaparadise.castyourinstructions.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gianlucaparadise.castyourinstructions.models.Routine

class RoutineDetailViewModel : ViewModel() {

    lateinit var mainViewModel: MainViewModel

    var routine: Routine? = null

    val castState: MutableLiveData<MainViewModel.CastStateValue>
        get() = mainViewModel.castState
}