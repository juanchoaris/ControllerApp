package com.example.controllerapp.data.repository

import com.example.controllerapp.data.remote.api.ApiService
import com.example.controllerapp.data.remote.model.LocalidadInfo
import com.example.controllerapp.utils.Constants
import com.example.controllerapp.utils.NetworkExceptionHandler
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para operaciones con localidades
 * Principio de Single Responsibility: maneja solo operaciones de localidades
 */
class LocalidadesRepository(
    private val apiService: ApiService
) {
    
    /**
     * Obtiene las localidades desde la API
     * El servidor retorna un array directo de localidades
     * Implementa manejo de excepciones con try/catch
     * @return Result con List de LocalidadInfo o Error
     */
    suspend fun fetchLocalidades(): Result<List<LocalidadInfo>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLocalidades()
            
            // Verificar código HTTP
            val errorMessage = NetworkExceptionHandler.checkHttpResponse(response)
            if (errorMessage != null) {
                return@withContext Result.Error(
                    Exception("HTTP ${response.code()}"),
                    errorMessage
                )
            }
            
            // Verificar que hay datos
            val localidadesList = response.body()
            if (localidadesList != null && localidadesList.isNotEmpty()) {
                Result.Success(localidadesList)
            } else {
                Result.Error(
                    Exception("Empty response"),
                    Constants.ErrorMessages.LOCALIDADES_LOAD_ERROR
                )
            }
            
        } catch (e: Exception) {
            Result.Error(
                e,
                NetworkExceptionHandler.handleException(e)
            )
        }
    }
}
