package com.example.controllerapp.data.repository

import com.example.controllerapp.data.remote.api.ApiService
import com.example.controllerapp.data.remote.model.LocalidadesResponse
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
     * Implementa manejo de excepciones con try/catch
     * @return Result con LocalidadesResponse o Error
     */
    suspend fun fetchLocalidades(): Result<LocalidadesResponse> = withContext(Dispatchers.IO) {
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
            val localidadesResponse = response.body()
            if (localidadesResponse != null && localidadesResponse.estado == true) {
                Result.Success(localidadesResponse)
            } else {
                Result.Error(
                    Exception("Empty or invalid response"),
                    localidadesResponse?.mensaje ?: Constants.ErrorMessages.LOCALIDADES_LOAD_ERROR
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
