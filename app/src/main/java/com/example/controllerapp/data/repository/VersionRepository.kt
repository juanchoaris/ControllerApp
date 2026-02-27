package com.example.controllerapp.data.repository

import com.example.controllerapp.data.remote.api.ApiService
import com.example.controllerapp.data.remote.model.VersionResponse
import com.example.controllerapp.utils.Constants
import com.example.controllerapp.utils.NetworkExceptionHandler
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para control de versiones
 * Principio de Single Responsibility: maneja solo verificación de versiones
 */
class VersionRepository(
    private val apiService: ApiService
) {
    
    /**
     * Obtiene la versión actual de la aplicación desde la API
     * Implementa manejo de excepciones con try/catch
     * @return Result con VersionResponse o Error
     */
    suspend fun checkAppVersion(): Result<VersionResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAppVersion()
            
            // Verificar código HTTP
            val errorMessage = NetworkExceptionHandler.checkHttpResponse(response)
            if (errorMessage != null) {
                return@withContext Result.Error(
                    Exception("HTTP ${response.code()}"),
                    errorMessage
                )
            }
            
            // Verificar que hay datos
            val versionResponse = response.body()
            if (versionResponse != null) {
                Result.Success(versionResponse)
            } else {
                Result.Error(
                    Exception("Empty response"),
                    Constants.ErrorMessages.VERSION_CHECK_ERROR
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
     * Compara la versión local con la versión remota
     * @param remoteVersion Versión obtenida de la API
     * @return Mensaje descriptivo de la comparación
     */
    fun compareVersions(remoteVersion: String): String {
        val localVersion = Constants.LOCAL_APP_VERSION
        
        return when {
            compareVersionStrings(localVersion, remoteVersion) < 0 -> {
                "La versión local ($localVersion) es inferior a la versión actual ($remoteVersion). Se recomienda actualizar."
            }
            compareVersionStrings(localVersion, remoteVersion) > 0 -> {
                "La versión local ($localVersion) es superior a la versión del servidor ($remoteVersion)."
            }
            else -> {
                "La aplicación está actualizada (versión $localVersion)."
            }
        }
    }
    
    /**
     * Compara dos strings de versión (ej: "1.0.0" vs "1.0.1")
     * @return -1 si v1 < v2, 0 si son iguales, 1 si v1 > v2
     */
    private fun compareVersionStrings(v1: String, v2: String): Int {
        val parts1 = v1.split(".").map { it.toIntOrNull() ?: 0 }
        val parts2 = v2.split(".").map { it.toIntOrNull() ?: 0 }
        
        val maxLength = maxOf(parts1.size, parts2.size)
        
        for (i in 0 until maxLength) {
            val part1 = parts1.getOrNull(i) ?: 0
            val part2 = parts2.getOrNull(i) ?: 0
            
            when {
                part1 < part2 -> return -1
                part1 > part2 -> return 1
            }
        }
        
        return 0
    }
}
