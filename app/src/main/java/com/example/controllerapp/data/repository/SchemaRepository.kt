package com.example.controllerapp.data.repository

import com.example.controllerapp.data.local.dao.SchemaTableDao
import com.example.controllerapp.data.local.entity.SchemaTableEntity
import com.example.controllerapp.data.remote.api.ApiService
import com.example.controllerapp.data.remote.model.SchemaResponse
import com.example.controllerapp.utils.Constants
import com.example.controllerapp.utils.NetworkExceptionHandler
import com.example.controllerapp.utils.Result
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para operaciones con el esquema de datos
 * Principio de Single Responsibility: maneja solo operaciones de esquema
 */
class SchemaRepository(
    private val apiService: ApiService,
    private val schemaTableDao: SchemaTableDao
) {
    
    private val gson = Gson()
    
    /**
     * Obtiene el esquema desde la API y lo almacena localmente
     * Implementa manejo de excepciones con try/catch
     * @return Result con SchemaResponse o Error
     */
    suspend fun fetchAndSaveSchema(): Result<SchemaResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getSchema(true)
            
            // Verificar código HTTP
            val errorMessage = NetworkExceptionHandler.checkHttpResponse(response)
            if (errorMessage != null) {
                return@withContext Result.Error(
                    Exception("HTTP ${response.code()}"),
                    errorMessage
                )
            }
            
            // Verificar que hay datos
            val schemaResponse = response.body()
            if (schemaResponse != null && schemaResponse.estado == true) {
                // Guardar en base de datos local
                saveSchemaToDatabase(schemaResponse)
                Result.Success(schemaResponse)
            } else {
                Result.Error(
                    Exception("Empty or invalid response"),
                    schemaResponse?.mensaje ?: Constants.ErrorMessages.SCHEMA_LOAD_ERROR
                )
            }
            
        } catch (e: Exception) {
            Result.Error(
                e,
                NetworkExceptionHandler.handleException(e)
            )
        }
    }
    
    /**
     * Guarda las tablas del esquema en la base de datos local
     * @param schemaResponse Respuesta del esquema
     */
    private suspend fun saveSchemaToDatabase(schemaResponse: SchemaResponse) {
        try {
            // Limpiar tablas existentes
            schemaTableDao.deleteAllTables()
            
            // Convertir y guardar nuevas tablas
            val entities = schemaResponse.tablas?.map { tabla ->
                SchemaTableEntity(
                    tableName = tabla.nombreTabla ?: "Unknown",
                    tableDescription = tabla.descripcion,
                    tableData = gson.toJson(tabla)
                )
            } ?: emptyList()
            
            if (entities.isNotEmpty()) {
                schemaTableDao.insertTables(entities)
            }
            
        } catch (e: Exception) {
            android.util.Log.e("SchemaRepository", "Error saving schema to database", e)
            throw e
        }
    }
    
    /**
     * Obtiene las tablas guardadas localmente
     * @return Result con lista de SchemaTableEntity o Error
     */
    suspend fun getLocalTables(): Result<List<SchemaTableEntity>> = withContext(Dispatchers.IO) {
        try {
            val tables = schemaTableDao.getAllTables()
            Result.Success(tables)
        } catch (e: Exception) {
            Result.Error(
                e,
                Constants.ErrorMessages.DATABASE_ERROR
            )
        }
    }
    
    /**
     * Obtiene el conteo de tablas almacenadas
     * @return Número de tablas
     */
    suspend fun getTablesCount(): Int = withContext(Dispatchers.IO) {
        try {
            schemaTableDao.getTablesCount()
        } catch (e: Exception) {
            0
        }
    }
}
