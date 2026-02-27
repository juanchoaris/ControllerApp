package com.example.controllerapp.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de respuesta para localidades
 */
data class LocalidadesResponse(
    @SerializedName("Localidades")
    val localidades: List<LocalidadInfo>?,
    @SerializedName("Mensaje")
    val mensaje: String?,
    @SerializedName("Estado")
    val estado: Boolean?
)

/**
 * Información de una localidad
 */
data class LocalidadInfo(
    @SerializedName("AbreviacionCiudad")
    val abreviacionCiudad: String?,
    @SerializedName("NombreCompleto")
    val nombreCompleto: String?,
    @SerializedName("CodigoDane")
    val codigoDane: String?,
    @SerializedName("IdLocalidad")
    val idLocalidad: Int?
)
