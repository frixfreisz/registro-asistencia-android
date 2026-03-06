package com.matecode.registro.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matecode.registro.data.informe.InformeMensual

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

        Spacer(modifier = Modifier.height(12.dp))

        Row {

            Text("N°", modifier = Modifier.width(40.dp))

            Text(
                "Alumno",
                modifier = Modifier.width(220.dp)
            )

            informe.alumnos.firstOrNull()?.asistencias?.keys?.forEach { dia ->

                val numeroDia = dia.takeLast(2)

                Text(
                    text = numeroDia,
                    modifier = Modifier.width(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "P",
                modifier = Modifier.width(40.dp)
            )

            Text(
                "A",
                modifier = Modifier.width(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // VARONES
        varones.forEachIndexed { index, alumno ->

            Row {

                Text(
                    text = String.format("%02d", index + 1),
                    modifier = Modifier.width(40.dp)
                )

                Text(
                    alumno.nombre,
                    modifier = Modifier.width(220.dp)
                )

                alumno.asistencias.values.forEach { simbolo ->

                    Text(
                        simbolo,
                        modifier = Modifier.width(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    alumno.totalAsistencias.toString(),
                    modifier = Modifier.width(40.dp)
                )

                Text(
                    alumno.totalInasistencias.toString(),
                    modifier = Modifier.width(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // MUJERES
        mujeres.forEachIndexed { index, alumno ->

            Row {

                Text(
                    text = String.format("%02d", index + 1),
                    modifier = Modifier.width(40.dp)
                )

                Text(
                    alumno.nombre,
                    modifier = Modifier.width(220.dp)
                )

                alumno.asistencias.values.forEach { simbolo ->

                    Text(
                        simbolo,
                        modifier = Modifier.width(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    alumno.totalAsistencias.toString(),
                    modifier = Modifier.width(40.dp)
                )

                Text(
                    alumno.totalInasistencias.toString(),
                    modifier = Modifier.width(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}