package com.example.controllerapp.utils

/**
 * Clase sellada para representar el resultado de operaciones
 * Implementa el principio de manejo de errores tipado
 * @param T tipo de dato en caso de éxito
 */
sealed class Result<out T> {
    /**
     * Resultado exitoso
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Resultado con error
     */
    data class Error(val exception: Exception, val message: String) : Result<Nothing>()
    
    /**
     * Estado de carga
     */
    object Loading : Result<Nothing>()
}

/**
 * Extensión para ejecutar código solo si el resultado es exitoso
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

/**
 * Extensión para ejecutar código solo si el resultado es error
 */
inline fun <T> Result<T>.onError(action: (Exception, String) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(exception, message)
    }
    return this
}
