package com.matecode.registro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alumnos")
data class AlumnoEntity(
    @PrimaryKey val dniId: String,
    val apellido: String,
    val nombre: String,
    val fechaNacimiento: String,
    val sexo: String,
    val direccion: String,
    val localidad: String,
    val provincia: String,
    val pais: String,
    val telefono: String,
    val responsableTutor: String,
    val telefonoResponsableTutor: String,
    val ocupacion: String,
    val observaciones: String?,
    val foto: String?,
    val gradoId: Long
)
