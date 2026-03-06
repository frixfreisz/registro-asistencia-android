package com.matecode.registro.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GradoCard(
    nombre: String,
    turno: String,
    mesCerrado: Boolean,
    onAsistencia: () -> Unit,
    onCalendario: () -> Unit,
    onCerrarMes: () -> Unit,
    onReabrirMes: () -> Unit,
    onInformeClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = nombre,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = turno,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (mesCerrado) "🔒 Mes cerrado" else "Mes abierto",
                color = if (mesCerrado) Color.Red else Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAsistencia,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir a Asistencia")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ActionIcon(
                    icon = Icons.Default.DateRange,
                    label = "Calendario",
                    onClick = onCalendario
                )

                ActionIcon(
                    icon = Icons.Default.Lock,
                    label = "Cerrar",
                    onClick = onCerrarMes
                )

                ActionIcon(
                    icon = Icons.Default.Edit,
                    label = "Reabrir",
                    onClick = onReabrirMes
                )

                ActionIcon(
                    icon = Icons.AutoMirrored.Filled.List,
                    label = "Informe",
                    onClick = onInformeClick
                )
            }
        }
    }
}