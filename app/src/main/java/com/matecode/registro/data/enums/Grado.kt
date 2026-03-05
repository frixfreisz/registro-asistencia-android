package com.matecode.registro.data.enums
enum class Grado {
    PRIMERO,
    SEGUNDO,
    TERCERO,
    CUARTO,
    QUINTO,
    SEXTO;

    fun displayName(): String {
        return when (this) {
            PRIMERO -> "1°"
            SEGUNDO -> "2°"
            TERCERO -> "3°"
            CUARTO -> "4°"
            QUINTO -> "5°"
            SEXTO -> "6°"
        }
    }
}