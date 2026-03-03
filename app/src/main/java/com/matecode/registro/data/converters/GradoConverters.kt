package com.matecode.registro.data.converters

import androidx.room.TypeConverter
import com.matecode.registro.data.enums.Division
import com.matecode.registro.data.enums.Turno

class GradoConverters {

    @TypeConverter
    fun fromTurno(turno: Turno): String = turno.name

    @TypeConverter
    fun toTurno(value: String): Turno = Turno.valueOf(value)

    @TypeConverter
    fun fromDivision(division: Division): String = division.name

    @TypeConverter
    fun toDivision(value: String): Division = Division.valueOf(value)
}
