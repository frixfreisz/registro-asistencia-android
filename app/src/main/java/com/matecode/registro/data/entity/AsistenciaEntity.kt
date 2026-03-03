package com.matecode.registro.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.matecode.registro.data.enums.EstadoAsistencia

@Entity(
    tableName = "asistencias",
    primaryKeys = ["alumnoDni", "fecha"],
    foreignKeys = [
        ForeignKey(
            entity = AlumnoEntity::class,
            parentColumns = ["dniId"],
            childColumns = ["alumnoDni"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("alumnoDni"),
        Index("fecha")
    ]
)
data class AsistenciaEntity(
    val alumnoDni: String,
    val fecha: String, // ISO-8601: yyyy-MM-dd
    val estado: EstadoAsistencia,
    val observacion: String? = null,
    val gradoId: Long
)

