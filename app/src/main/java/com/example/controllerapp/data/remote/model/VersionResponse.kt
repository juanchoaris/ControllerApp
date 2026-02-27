package com.example.controllerapp.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de respuesta para el control de versiones
 */
data class VersionResponse(
    @SerializedName("VersionApp")
    val versionApp: String?,
    @SerializedName("Mensaje")
    val mensaje: String?,
    @SerializedName("Estado")
    val estado: Boolean?
)
