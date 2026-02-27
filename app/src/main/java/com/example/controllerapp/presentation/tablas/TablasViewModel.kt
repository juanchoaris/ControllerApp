package com.example.controllerapp.presentation.tablas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.controllerapp.data.local.database.AppDatabase
import com.example.controllerapp.data.local.entity.SchemaTableEntity
import com.example.controllerapp.data.remote.api.RetrofitClient
import com.example.controllerapp.data.repository.SchemaRepository
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Tablas
 * Principio de Single Responsibility: solo maneja la lógica de presentación de tablas
 */
class TablasViewModel(
    private val schemaRepository: SchemaRepository
) : ViewModel() {
    
    private val _tablasState = MutableStateFlow<Result<List<SchemaTableEntity>>>(Result.Loading)
    val tablasState: StateFlow<Result<List<SchemaTableEntity>>> = _tablasState
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    /**
     * Carga las tablas desde la base de datos local
     */
    fun loadTables() {
        viewModelScope.launch {
            _loading.value = true
            
            try {
                when (val result = schemaRepository.getLocalTables()) {
                    is Result.Success -> {
                        _tablasState.value = Result.Success(result.data)
                    }
                    is Result.Error -> {
                        _tablasState.value = Result.Error(result.exception, result.message)
                    }
                    is Result.Loading -> {
                        // No action needed
                    }
                }
            } catch (e: Exception) {
                _tablasState.value = Result.Error(e, "Error al cargar tablas")
            } finally {
                _loading.value = false
            }
        }
    }
}

/**
 * Factory para crear TablasViewModel con dependencias
 */
class TablasViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TablasViewModel::class.java)) {
            val schemaRepository = SchemaRepository(
                RetrofitClient.apiServiceTesting,
                database.schemaTableDao()
            )
            
            @Suppress("UNCHECKED_CAST")
            return TablasViewModel(schemaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
