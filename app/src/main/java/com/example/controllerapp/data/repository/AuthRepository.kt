package com.example.controllerapp.data.repository

import com.example.controllerapp.data.local.dao.UserDao
import com.example.controllerapp.data.local.entity.UserEntity
import com.example.controllerapp.data.remote.api.ApiService
import com.example.controllerapp.data.remote.model.LoginRequest
import com.example.controllerapp.data.remote.model.LoginResponse
import com.example.controllerapp.utils.Constants
import com.example.controllerapp.utils.NetworkExceptionHandler
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para operaciones de autenticación
 * Principio de Single Responsibility: maneja solo lógica de autenticación
 * Principio de Dependency Inversion: depende de abstracciones (interfaces)
 */
class AuthRepository(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    
    /**
     * Autentica un usuario usando la API y guarda los datos localmente
     * Implementa manejo de excepciones con try/catch
     * @return Result con LoginResponse o Error
     */
    suspend fun authenticateUser(): Result<LoginResponse> = withContext(Dispatchers.IO) {
        try {
            // Preparar headers según especificación
            val headers = mapOf(
                "Usuario" to Constants.LoginHeaders.USUARIO,
                "Identificacion" to Constants.LoginHeaders.IDENTIFICACION,
                "Accept" to Constants.LoginHeaders.ACCEPT,
                "IdUsuario" to Constants.LoginHeaders.ID_USUARIO,
                "IdCentroServicio" to Constants.LoginHeaders.ID_CENTRO_SERVICIO,
                "NombreCentroServicio" to Constants.LoginHeaders.NOMBRE_CENTRO_SERVICIO,
                "IdAplicativoOrigen" to Constants.LoginHeaders.ID_APLICATIVO_ORIGEN,
                "Content-Type" to Constants.LoginHeaders.CONTENT_TYPE
            )
            
            // Preparar body
            val loginRequest = LoginRequest(
                usuario = Constants.Credentials.USUARIO_ENCODED,
                password = Constants.Credentials.PASSWORD_ENCODED
            )
            
            // Realizar llamada a la API
            val response = apiService.authenticateUser(headers, loginRequest)
            
            // Verificar código HTTP
            if (response.code() != Constants.HttpStatusCodes.OK) {
                val errorMessage = NetworkExceptionHandler.checkHttpResponse(response)
                    ?: Constants.ErrorMessages.AUTHENTICATION_FAILED
                return@withContext Result.Error(
                    Exception("HTTP ${response.code()}"),
                    errorMessage
                )
            }
            
            // Verificar respuesta exitosa
            val loginResponse = response.body()
            if (loginResponse != null && loginResponse.estado == true) {
                // Guardar usuario en base de datos local
                saveUserToDatabase(loginResponse)
                Result.Success(loginResponse)
            } else {
                Result.Error(
                    Exception("Authentication failed"),
                    loginResponse?.mensaje ?: Constants.ErrorMessages.AUTHENTICATION_FAILED
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
     * Guarda los datos del usuario en la base de datos local
     * @param loginResponse Respuesta del login
     */
    private suspend fun saveUserToDatabase(loginResponse: LoginResponse) {
        try {
            val userEntity = UserEntity(
                usuario = loginResponse.usuario ?: "",
                identificacion = loginResponse.identificacion ?: "",
                nombre = loginResponse.nombre ?: ""
            )
            userDao.insertUser(userEntity)
        } catch (e: Exception) {
            // Log error but don't fail the authentication
            android.util.Log.e("AuthRepository", "Error saving user to database", e)
        }
    }
    
    /**
     * Obtiene el usuario guardado localmente
     * @return UserEntity o null
     */
    suspend fun getLocalUser(): UserEntity? = withContext(Dispatchers.IO) {
        try {
            userDao.getUser()
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Error getting local user", e)
            null
        }
    }
    
    /**
     * Cierra sesión eliminando datos locales
     */
    suspend fun logout() = withContext(Dispatchers.IO) {
        try {
            userDao.deleteAllUsers()
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Error during logout", e)
        }
    }
}
