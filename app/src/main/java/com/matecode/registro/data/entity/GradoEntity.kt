package com.matecode.registro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matecode.registro.data.enums.Division
import com.matecode.registro.data.enums.Grado
import com.matecode.registro.data.enums.Turno
import java.util.Locale

@Entity(tableName = "grados")
data class GradoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val docenteId: Long,

    val grado: Grado,
    val division: Division,
    val turno: Turno,
){

}
