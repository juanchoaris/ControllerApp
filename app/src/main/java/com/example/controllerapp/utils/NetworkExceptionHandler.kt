package com.example.controllerapp.utils

import android.util.Log
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Utilidad para manejar excepciones de red de forma centralizada
 * Implementa el principio DRY (Don't Repeat Yourself)
 */
object NetworkExceptionHandler {
    
    private const val TAG = "NetworkExceptionHandler"
    
    /**
     * Maneja excepciones de red y retorna un mensaje amigable
     * @param exception Excepción capturada
     * @return Mensaje descriptivo del error
     */
    fun handleException(exception: Exception): String {
        Log.e(TAG, "Exception handled: ${exception.message}", exception)
        
        return when (exception) {
            is UnknownHostException -> {
                "No se pudo conectar al servidor. Verifique su conexión a internet."
            }
            is SocketTimeoutException -> {
                "Tiempo de espera agotado. El servidor no responde."
            }
            is IOException -> {
                "Error de comunicación con el servidor."
            }
            else -> {
                exception.message ?: Constants.ErrorMessages.UNKNOWN_ERROR
            }
        }
    }
    
    /**
     * Verifica si una respuesta HTTP fue exitosa
     * @param response Respuesta de Retrofit
     * @return Mensaje de error o null si fue exitosa
     */
    fun <T> checkHttpResponse(response: Response<T>): String? {
        return when {
            response.isSuccessful -> null
            response.code() == Constants.HttpStatusCodes.UNAUTHORIZED -> {
                "No autorizado. Verifique sus credenciales."
            }
            response.code() == Constants.HttpStatusCodes.FORBIDDEN -> {
                "Acceso prohibido."
            }
            response.code() == Constants.HttpStatusCodes.NOT_FOUND -> {
                "Recurso no encontrado."
            }
            response.code() >= Constants.HttpStatusCodes.INTERNAL_SERVER_ERROR -> {
                "Error del servidor (${response.code()}). Intente más tarde."
            }
            else -> {
                "Error HTTP ${response.code()}: ${response.message()}"
            }
        }
    }
}
