package com.matecode.registro.data.converters

import androidx.room.TypeConverter
import com.matecode.registro.data.enums.EstadoAsistencia

class AsistenciaConverters {

    @TypeConverter
    fun fromEstado(estado: EstadoAsistencia): String = estado.name

    @TypeConverter
    fun toEstado(value: String): EstadoAsistencia =
        EstadoAsistencia.valueOf(value)

}
