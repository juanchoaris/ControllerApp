package com.example.controllerapp.data.local.dao

import androidx.room.*
import com.example.controllerapp.data.local.entity.UserEntity

/**
 * Data Access Object para operaciones de usuario
 * Principio de Single Responsibility: solo maneja operaciones CRUD de usuario
 */
@Dao
interface UserDao {
    
    /**
     * Inserta un usuario en la base de datos
     * Si el usuario ya existe, lo reemplaza
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    /**
     * Obtiene el usuario autenticado actual
     * @return UserEntity o null si no hay usuario autenticado
     */
    @Query("SELECT * FROM user_info LIMIT 1")
    suspend fun getUser(): UserEntity?
    
    /**
     * Elimina todos los usuarios de la base de datos
     */
    @Query("DELETE FROM user_info")
    suspend fun deleteAllUsers()
}
