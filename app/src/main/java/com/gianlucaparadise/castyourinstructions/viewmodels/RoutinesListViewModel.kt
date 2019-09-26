package com.gianlucaparadise.castyourinstructions.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gianlucaparadise.castyourinstructions.models.Recipes
import com.gianlucaparadise.castyourinstructions.repositories.RecipeRepository


class RecipesListViewModel : ViewModel() {

    val recipesObservable: LiveData<Recipes> = RecipeRepository.instance.getRecipes()

}
