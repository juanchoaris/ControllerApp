package com.example.controllerapp.data.remote.api

import com.example.controllerapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Interceptor para simular respuestas de la API cuando el modo mock está habilitado
 * Solo simula los endpoints que NO funcionan:
 * - AuthenticaUsuarioApp (Login) - endpoint con problemas
 * - ObtenerEsquema (Esquema) - endpoint con problemas
 * 
 * Los endpoints funcionales se conectan al servidor real:
 * - ConsultarParametrosFramework (Versión) - endpoint funcional
 * - ObtenerLocalidadesRecogidas (Localidades) - endpoint funcional
 */
class MockInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!Constants.MOCK_MODE_ENABLED) {
            // Si el modo mock está deshabilitado, continuar con la llamada real
            return chain.proceed(chain.request())
        }
        
        val request = chain.request()
        val path = request.url.encodedPath
        
        // Solo simular respuestas para los endpoints que NO funcionan
        return when {
            // Endpoints que NO funcionan - usar MOCK
            path.contains("AuthenticaUsuarioApp") -> {
                createMockLoginResponse(chain)
            }
            path.contains("ObtenerEsquema") -> {
                createMockSchemaResponse(chain)
            }
            
            // Endpoints que SÍ funcionan - llamar al servidor real
            // - ConsultarParametrosFramework (versión)
            // - ObtenerLocalidadesRecogidas (localidades)
            else -> {
                // Hacer la llamada real al servidor
                chain.proceed(request)
            }
        }
    }
    
    /**
     * Crea respuesta mock para login
     */
    private fun createMockLoginResponse(chain: Interceptor.Chain): Response {
        val mockJson = """
            {
                "Usuario": "pam.meredy21",
                "Identificacion": "987204545",
                "Nombre": "MEREDY PAOLA ACERO MULCUE",
                "Token": "mock-token-12345-67890-abcdef",
                "Mensaje": "Autenticación exitosa (MOCK)",
                "Estado": true
            }
        """.trimIndent()
        
        return createMockResponse(chain, mockJson)
    }
    
    /**
     * Crea respuesta mock para esquema de datos
     */
    private fun createMockSchemaResponse(chain: Interceptor.Chain): Response {
        val mockJson = """
            {
                "Tablas": [
                    {
                        "NombreTabla": "Clientes",
                        "Descripcion": "Tabla de clientes del sistema",
                        "Campos": [
                            {
                                "NombreCampo": "Id",
                                "TipoDato": "INTEGER"
                            },
                            {
                                "NombreCampo": "Nombre",
                                "TipoDato": "TEXT"
                            },
                            {
                                "NombreCampo": "Identificacion",
                                "TipoDato": "TEXT"
                            },
                            {
                                "NombreCampo": "Telefono",
                                "TipoDato": "TEXT"
                            }
                        ]
                    },
                    {
                        "NombreTabla": "Pedidos",
                        "Descripcion": "Tabla de pedidos y órdenes",
                        "Campos": [
                            {
                                "NombreCampo": "Id",
                                "TipoDato": "INTEGER"
                            },
                            {
                                "NombreCampo": "NumeroOrden",
                                "TipoDato": "TEXT"
                            },
                            {
                                "NombreCampo": "ClienteId",
                                "TipoDato": "INTEGER"
                            },
                            {
                                "NombreCampo": "Fecha",
                                "TipoDato": "TEXT"
                            },
                            {
                                "NombreCampo": "Estado",
                                "TipoDato": "TEXT"
                            }
                        ]
                    },
                    {
                        "NombreTabla": "Productos",
                        "Descripcion": "Catálogo de productos",
                        "Campos": [
                            {
                                "NombreCampo": "Id",
                                "TipoDato": "INTEGER"
                            },
                            {
                                "NombreCampo": "Codigo",
                                "TipoDato": "TEXT"
                            },
                            {
                                "NombreCampo": "Descripcion",
                                "TipoDato": "TEXT"
                            },
                            {
                                "NombreCampo": "Precio",
                                "TipoDato": "REAL"
                            }
                        ]
                    }
                ],
                "Mensaje": "Esquema obtenido exitosamente (MOCK)",
                "Estado": true
            }
        """.trimIndent()
        
        return createMockResponse(chain, mockJson)
    }
    
    /**
     * Crea una respuesta HTTP mock con el JSON proporcionado
     */
    private fun createMockResponse(chain: Interceptor.Chain, jsonBody: String): Response {
        return Response.Builder()
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(chain.request())
            .body(jsonBody.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("Content-Type", "application/json")
            .build()
    }
}
