package com.example.controllerapp.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para comunicación con las APIs
 * Implementa patrón Singleton y principio de Open/Closed
 */
object RetrofitClient {
    
    private const val BASE_URL_TESTING = "https://apitesting.interrapidisimo.co/apicontrollerpruebas/"
    private const val BASE_URL_SECURITY = "https://apitesting.interrapidisimo.co/FtEntregaElectronica/MultiCanales/ApiSeguridadPruebas/"
    
    private const val TIMEOUT_SECONDS = 30L
    
    /**
     * Crea un cliente OkHttp con logging interceptor y mock interceptor
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(MockInterceptor()) // Interceptor para simular respuestas
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Cliente para API de testing (esquema, versiones, localidades)
     * Usa ScalarsConverterFactory primero para manejar respuestas String simples
     */
    val apiServiceTesting: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_TESTING)
            .client(createOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create()) // Para String responses
            .addConverterFactory(GsonConverterFactory.create()) // Para JSON responses
            .build()
            .create(ApiService::class.java)
    }
    
    /**
     * Cliente para API de seguridad (login)
     * Usa ScalarsConverterFactory primero para manejar respuestas String simples
     */
    val apiServiceSecurity: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_SECURITY)
            .client(createOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create()) // Para String responses
            .addConverterFactory(GsonConverterFactory.create()) // Para JSON responses
            .build()
            .create(ApiService::class.java)
    }
}
