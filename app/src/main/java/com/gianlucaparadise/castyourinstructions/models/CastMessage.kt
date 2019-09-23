package com.gianlucaparadise.castyourinstructions.models

data class CastMessage(val type: MessageType, val recipe: Recipe? = null)

enum class MessageType {
    LOAD,
    PLAY,
    PAUSE,
    STOP,
}