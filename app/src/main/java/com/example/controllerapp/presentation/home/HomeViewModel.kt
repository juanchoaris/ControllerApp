package com.example.controllerapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.controllerapp.data.local.database.AppDatabase
import com.example.controllerapp.data.local.entity.UserEntity
import com.example.controllerapp.data.remote.api.RetrofitClient
import com.example.controllerapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla Home
 * Principio de Single Responsibility: solo maneja la lógica de presentación del home
 */
class HomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _userState = MutableStateFlow<UserEntity?>(null)
    val userState: StateFlow<UserEntity?> = _userState
    
    /**
     * Carga los datos del usuario desde la base de datos local
     */
    fun loadUserData() {
        viewModelScope.launch {
            try {
                val user = authRepository.getLocalUser()
                _userState.value = user
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Error loading user data", e)
            }
        }
    }
}

/**
 * Factory para crear HomeViewModel con dependencias
 */
class HomeViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val authRepository = AuthRepository(
                RetrofitClient.apiServiceSecurity,
                database.userDao()
            )
            
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
