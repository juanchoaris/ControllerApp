package com.example.controllerapp.data.remote.api

import com.example.controllerapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Interceptor para simular respuestas de la API cuando el modo mock está habilitado
 * Útil para desarrollo y testing cuando el servidor tiene problemas
 */
class MockInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!Constants.MOCK_MODE_ENABLED) {
            // Si el modo mock está deshabilitado, continuar con la llamada real
            return chain.proceed(chain.request())
        }
        
        val request = chain.request()
        val path = request.url.encodedPath
        
        // Simular respuestas según el endpoint
        return when {
            path.contains("AuthenticaUsuarioApp") -> {
                createMockLoginResponse(chain)
            }
            path.contains("ConsultarParametrosFramework") -> {
                createMockVersionResponse(chain)
            }
            path.contains("ObtenerEsquema") -> {
                createMockSchemaResponse(chain)
            }
            path.contains("ObtenerLocalidadesRecogidas") -> {
                createMockLocalidadesResponse(chain)
            }
            else -> {
                // Para endpoints no mockeados, hacer la llamada real
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
     * Crea respuesta mock para versión
     */
    private fun createMockVersionResponse(chain: Interceptor.Chain): Response {
        val mockJson = """
            {
                "VersionApp": "1.0.0",
                "Mensaje": "Versión actual (MOCK)",
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
     * Crea respuesta mock para localidades
     */
    private fun createMockLocalidadesResponse(chain: Interceptor.Chain): Response {
        val mockJson = """
            {
                "Localidades": [
                    {
                        "IdLocalidad": 1,
                        "AbreviacionCiudad": "BOG",
                        "NombreCompleto": "Bogotá D.C.",
                        "CodigoDane": "11001"
                    },
                    {
                        "IdLocalidad": 2,
                        "AbreviacionCiudad": "MED",
                        "NombreCompleto": "Medellín",
                        "CodigoDane": "05001"
                    },
                    {
                        "IdLocalidad": 3,
                        "AbreviacionCiudad": "CAL",
                        "NombreCompleto": "Cali",
                        "CodigoDane": "76001"
                    },
                    {
                        "IdLocalidad": 4,
                        "AbreviacionCiudad": "BAQ",
                        "NombreCompleto": "Barranquilla",
                        "CodigoDane": "08001"
                    },
                    {
                        "IdLocalidad": 5,
                        "AbreviacionCiudad": "CTG",
                        "NombreCompleto": "Cartagena",
                        "CodigoDane": "13001"
                    },
                    {
                        "IdLocalidad": 6,
                        "AbreviacionCiudad": "BUC",
                        "NombreCompleto": "Bucaramanga",
                        "CodigoDane": "68001"
                    },
                    {
                        "IdLocalidad": 7,
                        "AbreviacionCiudad": "PER",
                        "NombreCompleto": "Pereira",
                        "CodigoDane": "66001"
                    },
                    {
                        "IdLocalidad": 8,
                        "AbreviacionCiudad": "CUC",
                        "NombreCompleto": "Cúcuta",
                        "CodigoDane": "54001"
                    }
                ],
                "Mensaje": "Localidades obtenidas exitosamente (MOCK)",
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
