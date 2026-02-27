package com.example.controllerapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.controllerapp.data.local.database.AppDatabase
import com.example.controllerapp.data.remote.api.RetrofitClient
import com.example.controllerapp.data.repository.AuthRepository
import com.example.controllerapp.data.repository.SchemaRepository
import com.example.controllerapp.data.repository.VersionRepository
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Login
 * Implementa principios SOLID:
 * - Single Responsibility: solo maneja la lógica de presentación del login
 * - Dependency Inversion: depende de abstracciones (repositorios)
 */
class LoginViewModel(
    private val versionRepository: VersionRepository,
    private val authRepository: AuthRepository,
    private val schemaRepository: SchemaRepository
) : ViewModel() {
    
    private val _versionState = MutableStateFlow<Result<String>>(Result.Loading)
    val versionState: StateFlow<Result<String>> = _versionState
    
    private val _loginState = MutableStateFlow<Result<Boolean>>(Result.Loading)
    val loginState: StateFlow<Result<Boolean>> = _loginState
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    /**
     * Verifica la versión de la aplicación
     */
    fun checkVersion() {
        viewModelScope.launch {
            _loading.value = true
            
            try {
                when (val result = versionRepository.checkAppVersion()) {
                    is Result.Success -> {
                        val remoteVersion = result.data.versionApp ?: "Unknown"
                        val message = versionRepository.compareVersions(remoteVersion)
                        _versionState.value = Result.Success(message)
                    }
                    is Result.Error -> {
                        _versionState.value = Result.Error(result.exception, result.message)
                    }
                    is Result.Loading -> {
                        // No action needed
                    }
                }
            } catch (e: Exception) {
                _versionState.value = Result.Error(e, "Error al verificar versión")
            } finally {
                _loading.value = false
            }
        }
    }
    
    /**
     * Autentica al usuario
     * Después del login exitoso, obtiene el esquema de datos
     */
    fun authenticate() {
        viewModelScope.launch {
            _loading.value = true
            
            try {
                // Autenticar usuario
                when (val authResult = authRepository.authenticateUser()) {
                    is Result.Success -> {
                        // Login exitoso, ahora obtener esquema
                        fetchSchema()
                    }
                    is Result.Error -> {
                        _loginState.value = Result.Error(authResult.exception, authResult.message)
                        _loading.value = false
                    }
                    is Result.Loading -> {
                        // No action needed
                    }
                }
            } catch (e: Exception) {
                _loginState.value = Result.Error(e, "Error en autenticación")
                _loading.value = false
            }
        }
    }
    
    /**
     * Obtiene el esquema de datos después del login
     */
    private suspend fun fetchSchema() {
        try {
            when (val schemaResult = schemaRepository.fetchAndSaveSchema()) {
                is Result.Success -> {
                    _loginState.value = Result.Success(true)
                }
                is Result.Error -> {
                    // Aunque falle el esquema, permitir continuar
                    _loginState.value = Result.Success(true)
                }
                is Result.Loading -> {
                    // No action needed
                }
            }
        } catch (e: Exception) {
            // Aunque falle el esquema, permitir continuar
            _loginState.value = Result.Success(true)
        } finally {
            _loading.value = false
        }
    }
}

/**
 * Factory para crear LoginViewModel con dependencias
 * Implementa el patrón Factory
 */
class LoginViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val versionRepository = VersionRepository(RetrofitClient.apiServiceTesting)
            val authRepository = AuthRepository(
                RetrofitClient.apiServiceSecurity,
                database.userDao()
            )
            val schemaRepository = SchemaRepository(
                RetrofitClient.apiServiceTesting,
                database.schemaTableDao()
            )
            
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(versionRepository, authRepository, schemaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
