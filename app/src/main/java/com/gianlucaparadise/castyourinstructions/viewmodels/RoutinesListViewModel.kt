package com.gianlucaparadise.castyourinstructions.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gianlucaparadise.castyourinstructions.models.Routines
import com.gianlucaparadise.castyourinstructions.repositories.RoutineRepository


class RoutinesListViewModel : ViewModel() {

    val routinesObservable: LiveData<Routines> = RoutineRepository.instance.getRoutines()

}
