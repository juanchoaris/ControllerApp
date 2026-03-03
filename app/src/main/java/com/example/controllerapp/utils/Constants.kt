package com.example.controllerapp.utils

import com.example.controllerapp.BuildConfig

/**
 * Constantes de la aplicación
 * Principio de Single Responsibility: centraliza todas las constantes
 */
object Constants {
    
    // Modo Mock - Cambiar a true para usar datos simulados (útil cuando el servidor tiene problemas)
    const val MOCK_MODE_ENABLED = true
    
    // Versión local de la aplicación (obtenida de BuildConfig)
    val LOCAL_APP_VERSION: String
        get() = BuildConfig.VERSION_NAME
    
    // Headers para autenticación
    object LoginHeaders {
        const val USUARIO = "pam.meredy21"
        const val IDENTIFICACION = "987204545"
        const val ACCEPT = "text/json"
        const val ID_USUARIO = "pam.meredy21"
        const val ID_CENTRO_SERVICIO = "1295"
        const val NOMBRE_CENTRO_SERVICIO = "PTO/BOGOTA/CUND/COL/OF PRINCIPAL - CRA 30 # 7-45"
        const val ID_APLICATIVO_ORIGEN = "9"
        const val CONTENT_TYPE = "application/json"
    }
    
    // Credenciales codificadas (Base64)
    object Credentials {
        const val USUARIO_ENCODED = "cGFtLm1lcmVkeTIx\n"
        const val PASSWORD_ENCODED = "SW50ZXIyMDIx\n"
    }
    
    // Mensajes de error
    object ErrorMessages {
        const val NETWORK_ERROR = "Error de red. Verifique su conexión a internet."
        const val SERVER_ERROR = "Error del servidor. Intente de nuevo más tarde."
        const val AUTHENTICATION_FAILED = "Autenticación fallida. Verifique sus credenciales."
        const val VERSION_CHECK_ERROR = "Error al verificar la versión de la aplicación."
        const val SCHEMA_LOAD_ERROR = "Error al cargar el esquema de datos."
        const val LOCALIDADES_LOAD_ERROR = "Error al cargar las localidades."
        const val DATABASE_ERROR = "Error al acceder a la base de datos local."
        const val UNKNOWN_ERROR = "Error desconocido. Intente de nuevo."
    }
    
    // Códigos HTTP
    object HttpStatusCodes {
        const val OK = 200
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val INTERNAL_SERVER_ERROR = 500
    }
}
