package com.matecode.registro.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GradosScreen(
    onGradoClick: (Long) -> Unit
) {

    val grados = listOf(
        1L to "4° C",
        2L to "5° C",
        3L to "6° C"
    )

    Column {

        Text(
            text = "REGISTRO DE ASISTENCIA 2026",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        grados.forEach { (id, nombre) ->

            Button(
                onClick = { onGradoClick(id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(nombre)
            }
        }
    }
}
