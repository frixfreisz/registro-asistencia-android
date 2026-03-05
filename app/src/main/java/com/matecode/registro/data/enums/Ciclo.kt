package com.matecode.registro.data.enums

enum class Ciclo {
    PRIMER,
    PRIMERO,
    SEGUNDO,
    TERCER,
    TERCERO;

    fun displayname(): String {
        return when (this){
            Ciclo.PRIMER -> "Primer"
            Ciclo.PRIMERO -> "Primero"
            Ciclo.SEGUNDO -> "Segundo"
            Ciclo.TERCER -> "Tercer"
            Ciclo.TERCERO -> "Tercero"


        }
    }
}