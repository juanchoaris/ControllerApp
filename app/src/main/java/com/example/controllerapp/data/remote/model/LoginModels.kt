package com.example.controllerapp.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de request para autenticación
 */
data class LoginRequest(
    @SerializedName("Mac")
    val mac: String = "",
    @SerializedName("NomAplicacion")
    val nomAplicacion: String = "Controller APP",
    @SerializedName("Password")
    val password: String,
    @SerializedName("Path")
    val path: String = "",
    @SerializedName("Usuario")
    val usuario: String
)

/**
 * Modelo de respuesta de autenticación
 */
data class LoginResponse(
    @SerializedName("Usuario")
    val usuario: String?,
    @SerializedName("Identificacion")
    val identificacion: String?,
    @SerializedName("Nombre")
    val nombre: String?,
    @SerializedName("Token")
    val token: String?,
    @SerializedName("Mensaje")
    val mensaje: String?,
    @SerializedName("Estado")
    val estado: Boolean?
)
