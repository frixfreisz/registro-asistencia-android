package com.matecode.registro.ui


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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.matecode.registro.data.enums.TipoDia
import com.matecode.registro.data.viewmodel.GradoViewModel
import java.time.LocalDate
import java.time.YearMonth
@Suppress("NewApi")
@Composable
fun AsistenciaScreen(
    viewModel: GradoViewModel,
    gradoId: Long,
    onVolver: () -> Unit,
    onCerrarMes: () -> Unit
) {
    var mesCerrado by remember { mutableStateOf(false) }

    var mostrarCierre by remember { mutableStateOf(false) }

    if (mostrarCierre) {
        CierreMesScreen(
            viewModel = viewModel,
            gradoId = gradoId,
            yearMonth = YearMonth.now(),
            onVolver = { mostrarCierre = false }
        )
        return
    }

    val dias by viewModel.dias.collectAsState()
    val alumnos by viewModel.alumnos.collectAsState()
    val asistencias by viewModel.asistenciasDelDia.collectAsState()
    val mostrarDialogo by viewModel.mostrarDialogoJustificacion.collectAsState()


    val fechaHoy = LocalDate.now().toString()

    LaunchedEffect(Unit) {

        mesCerrado = viewModel.mesEstaCerrado(
            gradoId,
            YearMonth.now()
        )
    }

    LaunchedEffect(gradoId) {
        viewModel.seleccionarGrado(gradoId, YearMonth.now())
        viewModel.cargarAsistenciasDelDia(fechaHoy)
        //viewModel.verificarDiaAnterior()

    }
    Text("Alumnos cargados: ${alumnos.size}")
    val diaActual = dias.find { it.fecha == fechaHoy }

    val puedeTomarAsistencia =
        diaActual?.tipoDia?.esDiaTrabajado() == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {

        AsistenciaHeader(
            fecha = fechaHoy,
            docente = "ELIANA GARCÍA",
            ciclo = "SEGUNDO CICLO",
            grado = "QUINTO \"C\"",
            tipoDiaActual = diaActual?.tipoDia,
            onVolver = onVolver,
            onCambiarTipoDia = { tipo ->
                viewModel.actualizarTipoDia(
                    gradoId = gradoId,
                    fecha = fechaHoy,
                    nuevoTipo = tipo
                )
            }
        )
      /**  if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("No se tomó asistencia ayer") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Seleccione el motivo:")

                        Button(
                            onClick = {
                                viewModel.justificarDiaAnterior(TipoDia.CAMBIO_ACTIVIDAD)

                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Actividad especial / Acto")
                        }

                        Button(
                            onClick = {
                                viewModel.justificarDiaAnterior(TipoDia.NO_LABORABLE)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Paro / Suspensión")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.ocultarDialogo()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Fue un error")
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
            Button(
                onClick = { mostrarCierre = true },
                modifier = Modifier.fillMaxWidth(),

                ) {
                Text("Ir a Cierre de Mes")
            }
        }
       **/

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        if (mesCerrado) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "⚠ MES CERRADO - NO SE PUEDE MODIFICAR LA ASISTENCIA",
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(alumnos) { alumno ->

                val asistenciaAlumno =
                    asistencias.find { it.alumnoDni == alumno.dniId }

                AlumnoRow(
                    alumno = alumno,
                    estadoActual = asistenciaAlumno?.estado,
                    enabled = puedeTomarAsistencia && !mesCerrado,
                    onGuardarAsistencia = { estado ->

                        viewModel.marcarAsistencia(
                            alumnoId = alumno.dniId,
                            fecha = fechaHoy,
                            estado = estado
                        )
                    }
                ){ estado ->
                    viewModel.marcarAsistencia(
                        alumnoId = alumno.dniId,
                        fecha = fechaHoy,
                        estado = estado
                    )
                }
            }
        }
    }
}

@Composable
fun AsistenciaHeader(
    fecha: String,
    docente: String,
    ciclo: String,
    grado: String,
    tipoDiaActual: TipoDia?,
    onVolver: () -> Unit,
    onCambiarTipoDia: (TipoDia) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var tipoSeleccionado by remember { mutableStateOf<TipoDia?>(null) }

    // 🔴 DIALOGO DE CONFIRMACIÓN
    if (mostrarDialogo && tipoSeleccionado != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Confirmar cambio") },
            text = {
                Text("¿Está seguro que desea cambiar el tipo de día? Esto puede afectar las asistencias registradas.")
            },
            confirmButton = {
                TextButton(onClick = {
                    onCambiarTipoDia(tipoSeleccionado!!)
                    mostrarDialogo = false
                }) {
                    Text("Sí, cambiar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                }

                Text(
                    text = fecha,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "REGISTRO DE ASISTENCIA",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("DOCENTE: $docente")
            Text("$ciclo - $grado")

            Spacer(modifier = Modifier.height(12.dp))

            Box {

                OutlinedButton(onClick = { expanded = true }) {
                    Text(tipoDiaActual?.displayName() ?: "Seleccionar tipo de día")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TipoDia.values().forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo.name) },
                            onClick = {
                                expanded = false
                                tipoSeleccionado = tipo
                                mostrarDialogo = true
                            }
                        )
                    }
                }
            }
        }
    }

}

