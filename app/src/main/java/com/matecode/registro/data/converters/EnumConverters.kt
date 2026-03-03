package com.matecode.registro.data.converters


import androidx.room.TypeConverter
import com.matecode.registro.data.enums.Division
import com.matecode.registro.data.enums.Grado
import com.matecode.registro.data.enums.Turno

class EnumConverters {

    // ---- Grado ----
    @TypeConverter
    fun fromGrado(value: Grado): String = value.name

    @TypeConverter
    fun toGrado(value: String): Grado = Grado.valueOf(value)

    // ---- Division ----
    @TypeConverter
    fun fromDivision(value: Division): String = value.name

    @TypeConverter
    fun toDivision(value: String): Division = Division.valueOf(value)

    // ---- Turno ----
    @TypeConverter
    fun fromTurno(value: Turno): String = value.name

    @TypeConverter
    fun toTurno(value: String): Turno = Turno.valueOf(value)
}