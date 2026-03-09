package com.matecode.registro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.matecode.registro.data.entity.GradoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GradoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(grado: GradoEntity): Long

    @Query("SELECT *  FROM  grados ORDER by grado, division, turno")
    fun getGrados(): Flow<List<GradoEntity>>

    @Query("SELECT * FROM grados")
    suspend fun obtenerTodos(): List<GradoEntity>

    @Query("SELECT * FROM grados ORDER BY grado, division")
    fun obtenerGrados(): Flow<List<GradoEntity>>

    @Query("SELECT * FROM grados WHERE id = :id")
    suspend fun obtenerPorId(id: Long): GradoEntity
}