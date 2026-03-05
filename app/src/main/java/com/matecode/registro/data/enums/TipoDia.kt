package com.matecode.registro.data.enums

enum class TipoDia () {
    CLASE_NORMAL, //DIA TRABAJADO
    JORNADA_INSTITUCIONAL,


    CAMBIO_ACTIVIDAD, //NO CUENTA
    NO_LABORABLE, //NO CUENTA
    FERIADO,//NO CUENTA
    PARO_DOCENTE,//NO CUENTA
    VACACIONES;

    fun displayName(): String{
        return when (this){
            TipoDia.CLASE_NORMAL -> "Clase normal"
            TipoDia.JORNADA_INSTITUCIONAL -> "Jornada"
            TipoDia.CAMBIO_ACTIVIDAD -> "Cambio de actividad"
            TipoDia.NO_LABORABLE -> "Fin de Semana"
            TipoDia.FERIADO -> "Feriado"
            TipoDia.PARO_DOCENTE -> "Paro docente"
            TipoDia.VACACIONES -> "Receso escolar"
        }
    }


    fun esDiaTrabajado(): Boolean {
        return this == CLASE_NORMAL ||
        this ==  JORNADA_INSTITUCIONAL

    }
}

