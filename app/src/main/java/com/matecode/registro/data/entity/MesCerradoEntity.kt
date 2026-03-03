package com.matecode.registro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meses_cerrados")
data class MesCerradoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val gradoId: Long,
    val yearMonth: String,
    val cerrado: Boolean
)
