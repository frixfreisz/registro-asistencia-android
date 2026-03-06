package com.matecode.registro.data.informe

import android.os.Build
import androidx.annotation.RequiresApi
import com.matecode.registro.data.entity.AlumnoEntity
import com.matecode.registro.data.entity.AsistenciaEntity
import com.matecode.registro.data.entity.DiaGradoEntity
import com.matecode.registro.data.enums.EstadoAsistencia
import java.time.YearMonth

object InformeMensualGenerator {

    @RequiresApi(Build.VERSION_CODES.O)
    fun generar(
        alumnos: List<AlumnoEntity>,
        asistencias: List<AsistenciaEntity>,
        dias: List<DiaGradoEntity>,
        yearMonth: YearMonth,
        grado: String,
        turno: String
    ): InformeMensual {

        val diasTrabajados = dias.count { it.tipoDia.esDiaTrabajado() }

        val alumnosOrdenados = alumnos.sortedWith(
            compareBy<AlumnoEntity> { it.sexo != "M" }
                .thenBy { it.apellido }
        )

        val listaInforme = mutableListOf<InformeAlumno>()

        alumnosOrdenados.forEachIndexed { index, alumno ->

            val numero = String.format("%02d", index + 1)

            val mapaCeldas = mutableMapOf<String, String>()

            var presentes = 0
            var ausentes = 0

            dias.forEach { dia ->

                val asistencia = asistencias.find {
                    it.alumnoDni == alumno.dniId &&
                            it.fecha == dia.fecha
                }

                val simbolo = simboloCelda(
                    dia.tipoDia,
                    asistencia?.estado
                )

                mapaCeldas[dia.fecha] = simbolo

                if (simbolo == "P") presentes++
                if (simbolo == "A") ausentes++
            }

            listaInforme.add(
                InformeAlumno(
                    numero = numero,
                    nombre = "${alumno.apellido} ${alumno.nombre}",
                    sexo = alumno.sexo,
                    asistencias = mapaCeldas,
                    totalAsistencias = presentes,
                    totalInasistencias = ausentes
                )
            )
        }

        return InformeMensual(
            grado = grado,
            turno = turno,
            mes = yearMonth.month.name,
            anio = yearMonth.year,
            diasTrabajados = diasTrabajados,
            alumnos = listaInforme
        )
    }

    private fun simboloCelda(
        tipoDia: com.matecode.registro.data.enums.TipoDia,
        estado: EstadoAsistencia?
    ): String {

        if (!tipoDia.esDiaTrabajado()) {
            return "-"
        }

        return when (estado) {

            EstadoAsistencia.PRESENTE -> "P"

            EstadoAsistencia.AUSENTE_JUSTIFICADA -> "A"

            EstadoAsistencia.AUSENTE_INJUSTIFICADA -> "A"

            EstadoAsistencia.DEFINIR_DESPUES -> "A"

            EstadoAsistencia.SIN_ASISTENCIA -> ""

            null -> ""
        }
    }
}