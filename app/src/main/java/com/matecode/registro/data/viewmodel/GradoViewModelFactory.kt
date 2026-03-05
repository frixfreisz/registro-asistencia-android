package com.matecode.registro.data.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matecode.registro.data.dao.AlumnoDao
import com.matecode.registro.data.dao.AsistenciaDao
import com.matecode.registro.data.dao.DiaGradoDao
import com.matecode.registro.data.dao.GradoDao
import com.matecode.registro.data.dao.MesCerradoDao

class GradoViewModelFactory(
    private val diaGradoDao: DiaGradoDao,
    private val alumnoDao: AlumnoDao,
    private val asistenciaDao: AsistenciaDao,
    private val mesCerradoDao: MesCerradoDao,
    private val gradoDao: GradoDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GradoViewModel::class.java)) {
            return GradoViewModel(
                diaGradoDao,
                alumnoDao,
                asistenciaDao,
                mesCerradoDao,
                gradoDao,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}