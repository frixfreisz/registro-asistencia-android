package com.matecode.registro.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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

@Composable
fun CeldaTabla(
    texto: String,
    ancho: Int,
    colorTexto: Color = Color.Black,
    alineacion: Alignment = Alignment.CenterStart
) {

    Box(
        modifier = Modifier
            .width(ancho.dp)
            .border(1.dp, Color.Black)
            .padding(start = 4.dp),
        contentAlignment = alineacion
    ) {

        Text(
            text = texto,
            color = colorTexto,
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



        // ENCABEZADO
        Row {

            CeldaTabla("N°", 40, alineacion = Alignment.Center)

            CeldaTabla("Alumno", 220, alineacion = Alignment.Center)

            informe.alumnos.firstOrNull()?.asistencias?.keys?.forEach { dia ->

                val numeroDia = dia.takeLast(2)

                CeldaTabla(numeroDia, 28)
            }

            CeldaTabla("P", 40, alineacion = Alignment.Center)

            CeldaTabla("A", 40, alineacion = Alignment.Center)
        }

     //   Spacer(modifier = Modifier.height(8.dp))

        // VARONES
        varones.forEachIndexed { index, alumno ->

            Row {

                CeldaTabla(String.format("%02d", index + 1), 40, )

                CeldaTabla(alumno.nombre, 220)

                alumno.asistencias.forEach { (_, simbolo) ->

                    CeldaTabla(
                        simbolo,
                        28, alineacion = Alignment.Center,
                        colorTexto = colorAsistencia(simbolo)
                    )
                }

                CeldaTabla(alumno.totalAsistencias.toString(), 40, alineacion = Alignment.Center)

                CeldaTabla(alumno.totalInasistencias.toString(), 40, alineacion = Alignment.Center)
            }
        }
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


        // MUJERES
        mujeres.forEachIndexed { index, alumno ->

            Row {

                CeldaTabla(String.format("%02d", index + 1), 40)

                CeldaTabla(alumno.nombre, 220)

                alumno.asistencias.forEach { (_, simbolo) ->

                    CeldaTabla(
                        simbolo,
                        28, alineacion = Alignment.Center,
                        colorTexto = colorAsistencia(simbolo)
                    )
                }

                CeldaTabla(alumno.totalAsistencias.toString(), 40, alineacion = Alignment.Center)

                CeldaTabla(alumno.totalInasistencias.toString(), 40, alineacion = Alignment.Center)
            }
        }
    }
}