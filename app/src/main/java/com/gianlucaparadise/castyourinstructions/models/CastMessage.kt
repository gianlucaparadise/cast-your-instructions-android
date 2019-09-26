package com.gianlucaparadise.castyourinstructions.models

data class CastMessage(val type: MessageType, val routine: Routine? = null)

enum class MessageType {
    LOAD,
    PLAY,
    PAUSE,
    STOP,
}