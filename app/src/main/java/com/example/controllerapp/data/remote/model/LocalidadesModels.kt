package com.example.controllerapp.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Información de una localidad
 * El servidor retorna un array directo de este modelo
 */
data class LocalidadInfo(
    @SerializedName("IdLocalidad")
    val idLocalidad: String?,
    @SerializedName("AbreviacionCiudad")
    val abreviacionCiudad: String?,
    @SerializedName("NombreCompleto")
    val nombreCompleto: String?,
    @SerializedName("NombreCorto")
    val nombreCorto: String?,
    @SerializedName("CodigoPostal")
    val codigoPostal: String?,
    @SerializedName("PermiteRecogida")
    val permiteRecogida: Boolean?,
    @SerializedName("IdCentroServicio")
    val idCentroServicio: Int?,
    @SerializedName("NombreAncestroPGrado")
    val nombreAncestroPGrado: String?
)
