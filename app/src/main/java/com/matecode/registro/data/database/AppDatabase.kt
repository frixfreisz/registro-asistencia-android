package com.matecode.registro.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.matecode.registro.data.converters.EnumConverters
import com.matecode.registro.data.converters.TipoDiaConverter
import com.matecode.registro.data.dao.AlumnoDao
import com.matecode.registro.data.dao.AsistenciaDao
import com.matecode.registro.data.dao.DiaGradoDao
import com.matecode.registro.data.dao.GradoDao
import com.matecode.registro.data.dao.MesCerradoDao
import com.matecode.registro.data.entity.AlumnoEntity
import com.matecode.registro.data.entity.AsistenciaEntity
import com.matecode.registro.data.entity.DiaGradoEntity
import com.matecode.registro.data.entity.GradoEntity
import com.matecode.registro.data.entity.MesCerradoEntity

@Database(
    entities = [
        AlumnoEntity::class,
        AsistenciaEntity::class,
        GradoEntity::class,
        DiaGradoEntity::class,
        MesCerradoEntity::class
    ],
    version = 19, // 🔥 SUBÍ CADA VEZ QUE CAMBIES UNA ENTITY
    exportSchema = false
)
@TypeConverters(
    TipoDiaConverter::class,
    EnumConverters::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alumnoDao(): AlumnoDao
    abstract fun asistenciaDao(): AsistenciaDao
    abstract fun gradoDao(): GradoDao
    abstract fun diaGradoDao(): DiaGradoDao
    abstract fun mesCerradoDao(): MesCerradoDao
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "registro_database"
                )
                    .fallbackToDestructiveMigration() // 🔥 SOLO PARA DESARROLLO
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
