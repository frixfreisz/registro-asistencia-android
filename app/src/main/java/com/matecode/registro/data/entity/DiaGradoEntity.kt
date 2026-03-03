package com.matecode.registro.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.matecode.registro.data.enums.TipoDia
@Entity(
    tableName = "dias_grado",
    primaryKeys = ["gradoId", "fecha"],
    indices = [Index("fecha")]

)
data class DiaGradoEntity(

    val gradoId: Long,
    val fecha: String,
    val tipoDia: TipoDia,
    val descripcion: String? = null
)

