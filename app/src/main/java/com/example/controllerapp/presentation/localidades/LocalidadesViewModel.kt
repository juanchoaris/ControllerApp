package com.example.controllerapp.presentation.localidades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.controllerapp.data.remote.api.RetrofitClient
import com.example.controllerapp.data.remote.model.LocalidadInfo
import com.example.controllerapp.data.repository.LocalidadesRepository
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Localidades
 * Principio de Single Responsibility: solo maneja la lógica de presentación de localidades
 */
class LocalidadesViewModel(
    private val localidadesRepository: LocalidadesRepository
) : ViewModel() {
    
    private val _localidadesState = MutableStateFlow<Result<List<LocalidadInfo>>>(Result.Loading)
    val localidadesState: StateFlow<Result<List<LocalidadInfo>>> = _localidadesState
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    /**
     * Carga las localidades desde la API
     */
    fun loadLocalidades() {
        viewModelScope.launch {
            _loading.value = true
            
            try {
                when (val result = localidadesRepository.fetchLocalidades()) {
                    is Result.Success -> {
                        // result.data ya es la lista directamente
                        _localidadesState.value = Result.Success(result.data)
                    }
                    is Result.Error -> {
                        _localidadesState.value = Result.Error(result.exception, result.message)
                    }
                    is Result.Loading -> {
                        // No action needed
                    }
                }
            } catch (e: Exception) {
                _localidadesState.value = Result.Error(e, "Error al cargar localidades")
            } finally {
                _loading.value = false
            }
        }
    }
}

/**
 * Factory para crear LocalidadesViewModel con dependencias
 */
class LocalidadesViewModelFactory : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalidadesViewModel::class.java)) {
            val localidadesRepository = LocalidadesRepository(
                RetrofitClient.apiServiceTesting
            )
            
            @Suppress("UNCHECKED_CAST")
            return LocalidadesViewModel(localidadesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
