package com.matecode.registro.data.dao


import androidx.room.*
import com.matecode.registro.data.entity.AlumnoEntity
import com.matecode.registro.data.entity.AsistenciaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlumnoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAlumno(alumno: AlumnoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAlumnos(alumnos: List<AlumnoEntity>)

    @Update
    suspend fun actualizarAlumno(alumno: AlumnoEntity)

    @Delete
    suspend fun eliminarAlumno(alumno: AlumnoEntity)



    @Query("SELECT * FROM alumnos WHERE dniId = :dni")
    suspend fun obtenerAlumnoPorDni(dni: String): AlumnoEntity?

    @Query("""
        SELECT * 
        FROM alumnos 
        WHERE gradoId = :gradoId
        ORDER BY apellido ASC
    """)
    fun getAlumnosPorGrado(gradoId: Long): Flow<List<AlumnoEntity>>

    @Query("""
    SELECT * FROM asistencias
    WHERE gradoId = :gradoId
    AND fecha BETWEEN :desde AND :hasta
    AND estado = 'DEFINIR_DESPUES'
""")
    suspend fun obtenerPendientesDelMes(
        gradoId: Long,
        desde: String,
        hasta: String
    ): List<AsistenciaEntity>

    @Query("SELECT COUNT(*) FROM alumnos")
    suspend fun contarAlumnos(): Int



}