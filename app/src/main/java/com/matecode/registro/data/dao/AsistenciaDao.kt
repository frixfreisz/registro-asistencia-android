package com.matecode.registro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matecode.registro.data.entity.AsistenciaEntity
import com.matecode.registro.data.entity.ResumenAsistencia
import kotlinx.coroutines.flow.Flow

@Dao
interface AsistenciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAsistencia(asistencia: AsistenciaEntity)

    @Query(
        """
        SELECT * FROM asistencias 
        WHERE fecha = :fecha
    """
    )
    suspend fun obtenerAsistenciasPorFecha(
        fecha: String
    ): List<AsistenciaEntity>

    @Query(
        """
        SELECT * FROM asistencias
        WHERE fecha = :fecha AND gradoId = :gradoId
    """
    )
    fun obtenerAsistenciasPorFechaYGrado(
        fecha: String,
        gradoId: Long
    ): Flow<List<AsistenciaEntity>>

    @Query(
        """
        SELECT alumnoDni,
               SUM(CASE WHEN estado = 'AUSENTE_JUSTIFICADA' THEN 1 ELSE 0 END) as justificadas,
               SUM(CASE WHEN estado = 'AUSENTE_INJUSTIFICADA' THEN 1 ELSE 0 END) as injustificadas
        FROM asistencias
        WHERE gradoId = :gradoId
        AND fecha BETWEEN :desde AND :hasta
        GROUP BY alumnoDni
    """
    )
    suspend fun resumenMensualPorAlumno(
        gradoId: Long,
        desde: String,
        hasta: String
    ): List<ResumenAsistencia>

    @Query(
        """
        SELECT COUNT(*) FROM asistencias
        WHERE gradoId = :gradoId
        AND fecha BETWEEN :desde AND :hasta
        AND estado = 'DEFINIR_DESPUES'
    """
    )
    suspend fun contarPendientesDelMes(
        gradoId: Long,
        desde: String,
        hasta: String
    ): Int

    @Query(
        """
        SELECT * FROM asistencias
        WHERE gradoId = :gradoId
        AND fecha BETWEEN :desde AND :hasta
        AND estado = 'DEFINIR_DESPUES'
    """
    )
    suspend fun obtenerPendientesDelMes(
        gradoId: Long,
        desde: String,
        hasta: String
    ): List<AsistenciaEntity>

    //---------------------------------
//-----RESUMEN DE ASISTENCIA PLANILLA
    //-----------------------------------

    @Query("""
SELECT * FROM asistencias
WHERE gradoId = :gradoId
AND fecha BETWEEN :desde AND :hasta
""")
    suspend fun obtenerAsistenciasDelMes(
        gradoId: Long,
        desde: String,
        hasta: String
    ): List<AsistenciaEntity>




}