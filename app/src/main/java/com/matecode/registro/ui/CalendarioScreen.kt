package com.matecode.registro.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matecode.registro.data.enums.TipoDia
import com.matecode.registro.data.viewmodel.GradoViewModel
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioScreen(
    viewModel: GradoViewModel,
    gradoId: Long,
    onVolver: () -> Unit
) {

    val dias by viewModel.dias.collectAsState()
    val estado by viewModel.estado.collectAsState()

    val yearMonth = remember { YearMonth.now() }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(gradoId) {
        viewModel.cargarDias(gradoId, yearMonth)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔹 Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            Text(
                text = "Calendario - $yearMonth",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Lista de días
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(dias) { dia ->

                val fechaLocal = java.time.LocalDate.parse(dia.fecha)
                val fechaFormateada = fechaLocal.format(
                    java.time.format.DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy")
                )
                val nombreDia = fechaLocal.dayOfWeek
                    .getDisplayName(
                        java.time.format.TextStyle.FULL,
                        java.util.Locale("es", "ES")
                    )
                    .replaceFirstChar { it.uppercase() }

                val color = when (dia.tipoDia) {
                    TipoDia.CLASE_NORMAL -> Color.White
                    TipoDia.JORNADA_INSTITUCIONAL -> Color(0xFFD0E8FF)
                    TipoDia.FERIADO -> Color(0xFFFFCDD2)
                    TipoDia.NO_LABORABLE -> Color(0xFFE0E0E0)
                    TipoDia.VACACIONES -> Color(0xFFC8E6C9)
                    TipoDia.CAMBIO_ACTIVIDAD -> Color(0xFFFFF9C4)
                    TipoDia.PARO_DOCENTE -> Color(0x80FF9800)
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = color),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {

                        // 🔹 Fecha + nombre del día (lado izquierdo)
                        Column {
                            Text("$fechaFormateada    \n\n$nombreDia")

                            if (dia.tipoDia != TipoDia.CLASE_NORMAL &&
                                dia.tipoDia != TipoDia.NO_LABORABLE
                            ) {
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                        // 🔹 Etiqueta centrada SOLO si no es normal o no laborable
                        if (dia.tipoDia != TipoDia.CLASE_NORMAL &&
                            dia.tipoDia != TipoDia.NO_LABORABLE
                        ) {

                            Text(
                                text = dia.tipoDia.name.replace("_", " "),
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // 🔹 Botón editar (lado derecho)
                        TextButton(
                            onClick = {
                                fechaSeleccionada = dia.fecha
                                mostrarDialogo = true
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Text("Editar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Botón cerrar mes
        Button(
            onClick = {
                viewModel.intentarCerrarMes()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Mes")
        }

        if (estado.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(estado)
        }
    }

    // 🔹 Diálogo selector tipo de día
    if (mostrarDialogo && fechaSeleccionada != null) {

        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Seleccionar tipo de día") },
            text = {
                Column {

                    TipoDia.values().forEach { tipo ->

                        Text(
                            text = tipo.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.actualizarTipoDia(
                                        gradoId,
                                        fechaSeleccionada!!,
                                        tipo
                                    )
                                    mostrarDialogo = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }
}

