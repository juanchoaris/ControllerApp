package com.example.controllerapp.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de respuesta para el esquema de tablas
 */
data class SchemaResponse(
    @SerializedName("Tablas")
    val tablas: List<TablaInfo>?,
    @SerializedName("Mensaje")
    val mensaje: String?,
    @SerializedName("Estado")
    val estado: Boolean?
)

/**
 * Información de una tabla del esquema
 */
data class TablaInfo(
    @SerializedName("NombreTabla")
    val nombreTabla: String?,
    @SerializedName("Descripcion")
    val descripcion: String?,
    @SerializedName("Campos")
    val campos: List<CampoInfo>?
)

/**
 * Información de un campo de tabla
 */
data class CampoInfo(
    @SerializedName("NombreCampo")
    val nombreCampo: String?,
    @SerializedName("TipoDato")
    val tipoDato: String?
)
