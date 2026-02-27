package com.example.controllerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa la información del usuario autenticado
 * Implementa el principio de Single Responsibility: solo maneja datos del usuario
 */
@Entity(tableName = "user_info")
data class UserEntity(
    @PrimaryKey
    val usuario: String,
    val identificacion: String,
    val nombre: String
)
