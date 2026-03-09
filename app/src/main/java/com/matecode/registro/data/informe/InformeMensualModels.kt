package com.matecode.registro.data.informe

import com.matecode.registro.data.enums.TipoDia

data class InformeAlumno(

    val numero: String,
    val nombre: String,
    val sexo: String,
    val asistencias: Map<String, Pair<String, TipoDia>>,
    val totalAsistencias: Int,
    val totalInasistencias: Int
)

data class InformeMensual(

    val grado: String,
    val turno: String,
    val mes: String,
    val anio: Int,
    val diasTrabajados: Int,
    val alumnos: List<InformeAlumno>
)