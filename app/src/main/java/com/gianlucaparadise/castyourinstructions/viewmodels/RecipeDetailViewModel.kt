package com.gianlucaparadise.castyourinstructions.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gianlucaparadise.castyourinstructions.models.Recipe

class RecipeDetailViewModel(recipe: Recipe?) : ViewModel() {

    val recipe: Recipe? = recipe

    class RecipeDetailViewModelFactory(val recipe: Recipe?) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecipeDetailViewModel(recipe) as T
        }

    }
}