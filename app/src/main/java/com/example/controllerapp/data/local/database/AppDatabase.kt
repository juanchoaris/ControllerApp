package com.example.controllerapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.controllerapp.data.local.dao.SchemaTableDao
import com.example.controllerapp.data.local.dao.UserDao
import com.example.controllerapp.data.local.entity.SchemaTableEntity
import com.example.controllerapp.data.local.entity.UserEntity

/**
 * Base de datos SQLite local de la aplicación
 * Implementa el patrón Singleton para garantizar una única instancia
 */
@Database(
    entities = [UserEntity::class, SchemaTableEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun schemaTableDao(): SchemaTableDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        private const val DATABASE_NAME = "controller_app_database"
        
        /**
         * Obtiene la instancia de la base de datos
         * Implementa Double-Check Locking para thread-safety
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
