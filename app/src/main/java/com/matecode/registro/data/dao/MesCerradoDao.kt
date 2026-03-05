package com.matecode.registro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matecode.registro.data.entity.MesCerradoEntity

@Dao
interface MesCerradoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cerrarMes(mes: MesCerradoEntity)

    @Query("""
        DELETE FROM mes_cerrado
        WHERE gradoId = :gradoId AND yearMonth = :yearMonth
    """)
    suspend fun reabrirMes(
        gradoId: Long,
        yearMonth: String
    )

    @Query("""
        SELECT * FROM mes_cerrado
        WHERE gradoId = :gradoId AND yearMonth = :yearMonth
        LIMIT 1
    """)
    suspend fun obtenerEstadoMes(
        gradoId: Long,
        yearMonth: String
    ): MesCerradoEntity?

    @Query("""
        SELECT COUNT(*) > 0
        FROM mes_cerrado
        WHERE gradoId = :gradoId AND yearMonth = :yearMonth
    """)
    suspend fun estaCerrado(
        gradoId: Long,
        yearMonth: String
    ): Boolean
}