package com.matecode.registro.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.matecode.registro.data.informe.InformeMensual
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun letraDia(fecha: String): String {

    val date = LocalDate.parse(fecha)

    return when (date.dayOfWeek) {
        java.time.DayOfWeek.MONDAY -> "L"
        java.time.DayOfWeek.TUESDAY -> "M"
        java.time.DayOfWeek.WEDNESDAY -> "X"
        java.time.DayOfWeek.THURSDAY -> "J"
        java.time.DayOfWeek.FRIDAY -> "V"
        java.time.DayOfWeek.SATURDAY -> "S"
        java.time.DayOfWeek.SUNDAY -> "D"
    }
}

@Composable
fun CeldaDia(
    numero: String,
    diaSemana: String
) {

    Column(
        modifier = Modifier
            .width(28.dp)
            .height(56.dp)
            .border(1.dp, Color.Black)
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = numero,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = diaSemana,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
@Composable
fun CeldaTabla(
    texto: String,
    ancho: Int,
    alto: Int = 28,
    colorTexto: Color = Color.Black,
    background: Color = Color.White,
    centrado: Boolean = false
) {

    Box(
        modifier = Modifier
            .width(ancho.dp)
            .height(alto.dp)
            .border(1.dp, Color.Black)
            .background(background)
            .padding(horizontal = 4.dp),
        contentAlignment = if (centrado) Alignment.Center else Alignment.CenterStart
    ) {

        Text(
            text = texto,
            color = colorTexto,
            textAlign = if (centrado) TextAlign.Center else TextAlign.Start,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

fun colorAsistencia(simbolo: String): Color {

    return when (simbolo) {

        "P" -> Color(0xFF2E7D32)

        "A" -> Color(0xFFC62828)

        "-" -> Color.Gray

        else -> Color.Black
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InformeMensualScreen(
    informe: InformeMensual
) {

    val horizontalScroll = rememberScrollState()
    val verticalScroll = rememberScrollState()

    val varones = informe.alumnos.filter { it.sexo == "M" }
    val mujeres = informe.alumnos.filter { it.sexo == "F" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .horizontalScroll(horizontalScroll)
            .verticalScroll(verticalScroll)
    ) {

        Text(
            text = "Informe mensual ${informe.mes} ${informe.anio}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        /*
        ENCABEZADO IGUAL A LA PLANILLA
         */

        Row {

            CeldaTabla("Grado y División", 260)

            CeldaTabla("Ciclo", 113)

            CeldaTabla("Mes", 168)

            CeldaTabla("Ciclo Lectivo", 140)

            CeldaTabla("Días Trabajados", 337)
        }

        Row {

            CeldaTabla(informe.grado, 260, centrado = true)

            CeldaTabla("Segundo", 113, centrado = true)

            CeldaTabla(informe.mes, 168, centrado = true)

            CeldaTabla(informe.anio.toString(), 140, centrado = true)

            CeldaTabla(informe.diasTrabajados.toString(), 337, centrado = true)
        }

        Row {

            CeldaTabla("Turno: ${informe.turno}", 1018)
        }

        Spacer(modifier = Modifier.height(10.dp))

        /*
        CABECERA DE LA TABLA
         */

        Row {

            CeldaTabla("N°", 40, 56, centrado = true)

            CeldaTabla("Alumno", 220, 56, centrado = true)

            informe.alumnos.firstOrNull()?.asistencias?.keys?.forEach { dia ->

                val numeroDia = dia.takeLast(2)

                CeldaDia(
                    numero = numeroDia,

                    diaSemana = letraDia(dia)


                )
            }

            CeldaTabla("P", 40, 56, centrado = true)

            CeldaTabla("A", 40, 56, centrado = true)
        }


        /*
        VARONES
         */

        varones.forEachIndexed { index, alumno ->

            Row {

                CeldaTabla(String.format("%02d", index + 1), 40, centrado = true)

                CeldaTabla(alumno.nombre, 220)

                alumno.asistencias.forEach { (_, dato) ->

                    val simbolo = dato.first

                    CeldaTabla(
                        simbolo,
                        28,
                        colorTexto = colorAsistencia(simbolo), centrado = true
                    )
                }

                CeldaTabla(alumno.totalAsistencias.toString(), 40, centrado = true)

                CeldaTabla(alumno.totalInasistencias.toString(), 40, centrado = true)
            }
        }

        /*
        FILA SEPARADORA
         */

        val cantidadDias = informe.alumnos.firstOrNull()?.asistencias?.size ?: 0

        Row {

            CeldaTabla("", 40)

            CeldaTabla("", 220)

            repeat(cantidadDias) {
                CeldaTabla("", 28)
            }

            CeldaTabla("", 40)

            CeldaTabla("", 40)
        }

        /*
        MUJERES
         */

        mujeres.forEachIndexed { index, alumno ->

            Row {

                CeldaTabla(String.format("%02d", index + 1), 40, centrado = true)

                CeldaTabla(alumno.nombre, 220)

                alumno.asistencias.forEach { (_, dato) ->

                    val simbolo = dato.first

                    CeldaTabla(
                        simbolo,
                        28,
                        colorTexto = colorAsistencia(simbolo),
                        centrado = true
                    )
                }

                CeldaTabla(alumno.totalAsistencias.toString(), 40, centrado = true)

                CeldaTabla(alumno.totalInasistencias.toString(), 40, centrado = true)
            }
        }
    }
}