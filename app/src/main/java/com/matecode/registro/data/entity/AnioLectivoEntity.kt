package com.matecode.registro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

class AnioLectivoEntity {
    @Entity(tableName = "anio_lectivo")
    data class AnioLectivo(
        @PrimaryKey
        val anio: Int,
    )
}
