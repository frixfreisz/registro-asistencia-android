package com.matecode.registro.data.enums

enum class Turno {
    MANIANA,
    TARDE,

    NOCHE;

    fun displayName(): String {
        return when (this) {
            MANIANA -> "Mañana"
            TARDE -> "Tarde"
            NOCHE -> "Noche"
        }
    }
}