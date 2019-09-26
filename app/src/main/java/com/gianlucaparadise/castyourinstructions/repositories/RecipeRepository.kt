package com.gianlucaparadise.castyourinstructions.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gianlucaparadise.castyourinstructions.R
import com.gianlucaparadise.castyourinstructions.application.MyApplication
import com.gianlucaparadise.castyourinstructions.models.Routines
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class RoutineRepository {

    val TAG = "RoutineRepository"

    fun getRoutines(): LiveData<Routines> {

        val data = MutableLiveData<Routines>()

        try {
            val raw = MyApplication.instance.resources.openRawResource(R.raw.routines)
            val reader = BufferedReader(InputStreamReader(raw))
            val gson = Gson()
            val routines = gson.fromJson(reader, Routines::class.java)

            data.value = routines

        } catch (ex: Exception) {
            Log.e(TAG, "Error while loading routines: $ex")
        }

        return data
    }

    companion object {
        val instance = RoutineRepository()
    }
}