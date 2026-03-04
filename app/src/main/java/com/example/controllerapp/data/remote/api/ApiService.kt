package com.example.controllerapp.data.remote.api

import com.example.controllerapp.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface que define los endpoints de la API
 * Principio de Interface Segregation: cada servicio tiene su propia interface
 */
interface ApiService {
    
    /**
     * Consulta la versión actual del aplicativo
     * El servidor retorna un String simple, no un objeto JSON
     * @return Response con String de la versión
     */
    @GET("api/ParametrosFramework/ConsultarParametrosFramework/VPStoreAppControl")
    suspend fun getAppVersion(): Response<String>
    
    /**
     * Autentica un usuario en el sistema
     * @param headers Headers específicos para la autenticación
     * @param request Datos de login
     * @return Response con LoginResponse
     */
    @POST("api/Seguridad/AuthenticaUsuarioApp")
    suspend fun authenticateUser(
        @HeaderMap headers: Map<String, String>,
        @Body request: LoginRequest
    ): Response<LoginResponse>
    
    /**
     * Obtiene el esquema de tablas
     * @param includeAll Parámetro para incluir todas las tablas
     * @return Response con SchemaResponse
     */
    @GET("api/SincronizadorDatos/ObtenerEsquema/{includeAll}")
    suspend fun getSchema(
        @Path("includeAll") includeAll: Boolean
    ): Response<SchemaResponse>
    
    /**
     * Obtiene las localidades de recogida
     * @return Response con LocalidadesResponse
     */
    @GET("api/ParametrosFramework/ObtenerLocalidadesRecogidas")
    suspend fun getLocalidades(): Response<LocalidadesResponse>
}
