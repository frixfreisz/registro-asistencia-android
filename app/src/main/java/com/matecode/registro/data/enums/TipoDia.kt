package com.matecode.registro.data.enums

enum class TipoDia () {
    CLASE_NORMAL, //DIA TRABAJADO
    JORNADA_INSTITUCIONAL,


    CAMBIO_ACTIVIDAD, //NO CUENTA
    NO_LABORABLE, //NO CUENTA
    FERIADO,//NO CUENTA
    PARO_DOCENTE,//NO CUENTA
    VACACIONES;


    fun esDiaTrabajado(): Boolean {
        return this == CLASE_NORMAL ||
        this ==  JORNADA_INSTITUCIONAL

    }
}

