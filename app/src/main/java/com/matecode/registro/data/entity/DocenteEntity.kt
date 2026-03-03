package com.matecode.registro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matecode.registro.data.enums.CargoEnum

@Entity(tableName = "docentes")
data class DocenteEntity(
    @PrimaryKey()
    val dniId: String,
    val apellido: String,
    val nombre: String,
    val cargo: CargoEnum,
    val direccion: String,
    val localidad: String,
    val provincia: String,
    val telefono: String,


    )