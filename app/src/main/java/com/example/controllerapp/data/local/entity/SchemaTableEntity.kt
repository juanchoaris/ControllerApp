package com.example.controllerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una tabla del esquema
 * Almacena las tablas retornadas por el servicio web de sincronización
 */
@Entity(tableName = "schema_tables")
data class SchemaTableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tableName: String,
    val tableDescription: String? = null,
    val tableData: String? = null // JSON string para almacenar datos adicionales
)
