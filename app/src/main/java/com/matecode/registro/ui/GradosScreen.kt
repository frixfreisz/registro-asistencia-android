package com.matecode.registro.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.matecode.registro.data.entity.GradoEntity
import com.matecode.registro.data.viewmodel.GradoViewModel
import com.matecode.registro.ui.components.GradoCard
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GradosScreen(
    grados: List<GradoEntity>,
    viewModel: GradoViewModel,
    onAbrirAsistencia: (Long) -> Unit,
    onAbrirCalendario: (Long) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        items(grados) { grado ->

            GradoCard(
                nombre = "${grado.grado.displayName()} ${grado.division.displayName()}",
                turno = grado.turno.displayName(),
                mesCerrado = false, // después lo conectamos con la BD

                onAsistencia = {
                    onAbrirAsistencia(grado.id)
                },

                onCalendario = {
                    onAbrirCalendario(grado.id)
                },

                onCerrarMes = {
                    viewModel.cerrarMes(grado.id, YearMonth.now())
                },

                onReabrirMes = {
                    viewModel.reabrirMes(grado.id, YearMonth.now())
                }
            )
        }
    }
}