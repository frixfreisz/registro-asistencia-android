package com.matecode.registro.ui



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matecode.registro.data.entity.AlumnoEntity
import com.matecode.registro.data.enums.EstadoAsistencia



@Composable
fun AlumnoRow(
    alumno: AlumnoEntity,
    estadoActual: EstadoAsistencia?,
    enabled: Boolean,
    onGuardarAsistencia: (EstadoAsistencia) -> Unit,
    onEstadoChange: (EstadoAsistencia) -> Unit
) {

    var seleccionado by rememberSaveable(estadoActual) {
        mutableStateOf(estadoActual)
    }

    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE9E9EF)
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${alumno.apellido},",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = alumno.nombre,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // 🔹 PRESENTE
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Presente")
                    val mesCerrado = false
                    RadioButton(
                        selected = seleccionado == EstadoAsistencia.PRESENTE,
                        onClick = {
                            onEstadoChange(EstadoAsistencia.PRESENTE)
                        },
                        enabled = !mesCerrado
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 🔹 AUSENTE
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ausente")
                    RadioButton(
                        selected =
                            seleccionado == EstadoAsistencia.AUSENTE_JUSTIFICADA ||
                                    seleccionado == EstadoAsistencia.AUSENTE_INJUSTIFICADA ||
                                    seleccionado == EstadoAsistencia.DEFINIR_DESPUES,
                        enabled = enabled,
                        onClick = {
                            if (!enabled) return@RadioButton
                            expanded = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val colorBoton = when (seleccionado) {
                    EstadoAsistencia.PRESENTE -> Color(0xFF388E3C)
                    EstadoAsistencia.AUSENTE_JUSTIFICADA -> Color(0xFFE53935)
                    EstadoAsistencia.AUSENTE_INJUSTIFICADA -> Color(0xFFE53935)
                    EstadoAsistencia.DEFINIR_DESPUES -> Color(0xFFFB8C00)
                    else -> Color.Gray
                }

                val textoBoton = when (seleccionado) {
                    EstadoAsistencia.PRESENTE -> "PRESENTE"
                    EstadoAsistencia.AUSENTE_JUSTIFICADA -> "AUSENTE JUSTIFICADA"
                    EstadoAsistencia.AUSENTE_INJUSTIFICADA -> "AUSENTE INJUSTIFICADA"
                    EstadoAsistencia.DEFINIR_DESPUES -> "DEFINIR DESPUÉS"
                    else -> "SIN DEFINIR"
                }

                Button(
                    onClick = { },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorBoton,
                        contentColor = Color.White,
                        disabledContainerColor = colorBoton,

                    ),

                    modifier = Modifier.weight(1f)
                ) {
                    Text(textoBoton)
                }

                if (enabled) {

                    Spacer(modifier = Modifier.width(8.dp))

                    Box {

                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {

                            DropdownMenuItem(
                                text = { Text("JUSTIFICADA") },
                                onClick = {
                                    seleccionado = EstadoAsistencia.AUSENTE_JUSTIFICADA
                                    onGuardarAsistencia(EstadoAsistencia.AUSENTE_JUSTIFICADA)
                                    expanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("INJUSTIFICADA") },
                                onClick = {
                                    seleccionado = EstadoAsistencia.AUSENTE_INJUSTIFICADA
                                    onGuardarAsistencia(EstadoAsistencia.AUSENTE_INJUSTIFICADA)
                                    expanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("DEFINIR DESPUÉS") },
                                onClick = {
                                    seleccionado = EstadoAsistencia.DEFINIR_DESPUES
                                    onGuardarAsistencia(EstadoAsistencia.DEFINIR_DESPUES)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


