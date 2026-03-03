package com.matecode.registro.data.converters

import androidx.room.TypeConverter
import com.matecode.registro.data.enums.TipoDia

class TipoDiaConverter {

    @TypeConverter
    fun fromTipoDia(tipo: TipoDia): String {
        return tipo.name
    }

    @TypeConverter
    fun toTipoDia(value: String): TipoDia {
        return TipoDia.valueOf(value)
    }
}
