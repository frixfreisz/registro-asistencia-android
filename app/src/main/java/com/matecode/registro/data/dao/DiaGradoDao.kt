package com.matecode.registro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matecode.registro.data.entity.DiaGradoEntity
import com.matecode.registro.data.enums.TipoDia

@Dao
interface DiaGradoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDia(dia: DiaGradoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDias(dias: List<DiaGradoEntity>)

    @Query("""
    UPDATE dias_grado
    SET tipoDia = :nuevoTipo
    WHERE gradoId = :gradoId
    AND fecha = :fecha
""")
    suspend fun actualizarTipoDia(
        gradoId: Long,
        fecha: String,
        nuevoTipo: TipoDia
    )


    @Query("""
        SELECT * FROM dias_grado
        WHERE gradoId = :gradoId
        AND fecha BETWEEN :desde AND :hasta
        ORDER BY fecha
    """)
    suspend fun obtenerDiasPorGrado(
        gradoId: Long,
        desde: String,
        hasta: String
    ): List<DiaGradoEntity>

    @Query("""
    SELECT COUNT(*) FROM dias_grado 
    WHERE gradoId = :gradoId 
    AND substr(fecha, 1, 7) = :yearMonth
""")
    suspend fun contarDiasDelMes(
        gradoId: Long,
        yearMonth: String
    ): Int




}