package com.matecode.registro.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matecode.registro.data.viewmodel.GradoViewModel
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CierreMesScreen(
    viewModel: GradoViewModel,
    gradoId: Long,
    yearMonth: YearMonth,
    onVolver: () -> Unit
) {

    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Cierre del mes: ${yearMonth.month}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                viewModel.intentarCerrarMes(
                    gradoId,
                    yearMonth
                ) { puedeCerrar ->

                    if (puedeCerrar) {
                        viewModel.cerrarMes(gradoId, yearMonth)
                        mensaje = "Mes cerrado correctamente"
                    } else {
                        mensaje = "No se puede cerrar el mes: hay asistencias pendientes"
                    }
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar mes")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.reabrirMes(gradoId, yearMonth)
                mensaje = "Mes reabierto"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reabrir mes")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(mensaje)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}

