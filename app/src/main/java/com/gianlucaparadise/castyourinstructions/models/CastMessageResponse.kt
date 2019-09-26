package com.gianlucaparadise.castyourinstructions.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CastMessageResponse : Serializable {

    @SerializedName("type")
    @Expose
    var type: ResponseMessageType? = null

    @SerializedName("routine")
    @Expose
    var routine: Routine? = null

    @SerializedName("selectedInstructionIndex")
    @Expose
    var selectedInstructionIndex: Int? = null
}

enum class ResponseMessageType {
    LOADED,
    PLAYED,
    PAUSED,
    STOPPED,
    SELECTED_INSTRUCTION,
}