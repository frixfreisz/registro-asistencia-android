package com.matecode.registro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matecode.registro.data.entity.MesCerradoEntity

@Dao
interface MesCerradoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarEstadoMes(mes: MesCerradoEntity)

    @Query("""
        SELECT * FROM meses_cerrados
        WHERE gradoId = :gradoId
        AND yearMonth = :yearMonth
    """)
    suspend fun obtenerEstadoMes(
        gradoId: Long,
        yearMonth: String
    ): MesCerradoEntity?
}
