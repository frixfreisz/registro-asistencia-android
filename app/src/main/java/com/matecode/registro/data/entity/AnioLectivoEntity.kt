package com.matecode.registro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anio_lectivo")
data class AnioLectivoEntity(
    @PrimaryKey
    val anio: Int
)