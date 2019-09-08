package com.gianlucaparadise.castyourinstructions.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Instruction {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("countdown")
    @Expose
    var countdown: Int? = null
    @SerializedName("videoUrl")
    @Expose
    var videoUrl: String? = null

}

class Recipe {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("source")
    @Expose
    var source: String? = null
    @SerializedName("instructions")
    @Expose
    var instructions: List<Instruction>? = null

}

class Recipes : ArrayList<Recipe>() {}