package com.gianlucaparadise.castyourinstructions.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.application.MyApplication
import com.gianlucaparadise.castyourinstructions.models.Recipes
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class RecipeRepository {

    val TAG = "RecipeRepository"

    fun getRecipes(): LiveData<Recipes> {

        val data = MutableLiveData<Recipes>()

        try {
            val raw = MyApplication.instance.resources.openRawResource(R.raw.recipes)
            val reader = BufferedReader(InputStreamReader(raw))
            val gson = Gson()
            val recipes = gson.fromJson(reader, Recipes::class.java)

            data.value = recipes

        } catch (ex: Exception) {
            Log.e(TAG, "Error while loading recipes: $ex")
        }

        return data
    }

    companion object {
        val instance = RecipeRepository()
    }
}