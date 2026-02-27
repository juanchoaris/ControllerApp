package com.example.controllerapp.data.local.dao

import androidx.room.*
import com.example.controllerapp.data.local.entity.SchemaTableEntity

/**
 * Data Access Object para operaciones de tablas del esquema
 * Principio de Single Responsibility: solo maneja operaciones CRUD de tablas
 */
@Dao
interface SchemaTableDao {
    
    /**
     * Inserta una lista de tablas en la base de datos
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTables(tables: List<SchemaTableEntity>)
    
    /**
     * Obtiene todas las tablas almacenadas
     * @return Lista de SchemaTableEntity
     */
    @Query("SELECT * FROM schema_tables ORDER BY tableName")
    suspend fun getAllTables(): List<SchemaTableEntity>
    
    /**
     * Elimina todas las tablas de la base de datos
     */
    @Query("DELETE FROM schema_tables")
    suspend fun deleteAllTables()
    
    /**
     * Obtiene el conteo de tablas almacenadas
     */
    @Query("SELECT COUNT(*) FROM schema_tables")
    suspend fun getTablesCount(): Int
}
