package com.example.controllerapp.presentation.localidades

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controllerapp.databinding.ActivityLocalidadesBinding
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.launch

/**
 * Activity de Localidades
 * Consume y exhibe las localidades de recogida
 */
class LocalidadesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLocalidadesBinding
    private val viewModel: LocalidadesViewModel by viewModels {
        LocalidadesViewModelFactory()
    }
    private lateinit var adapter: LocalidadesAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Localidades"
        
        setupRecyclerView()
        observeViewModel()
        
        // Cargar localidades
        viewModel.loadLocalidades()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    /**
     * Configura el RecyclerView
     */
    private fun setupRecyclerView() {
        adapter = LocalidadesAdapter()
        binding.recyclerViewLocalidades.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLocalidades.adapter = adapter
    }
    
    /**
     * Observa los cambios en el ViewModel
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.localidadesState.collect { result ->
                handleLocalidadesResult(result)
            }
        }
        
        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                showLoading(isLoading)
            }
        }
    }
    
    /**
     * Maneja el resultado de la carga de localidades
     */
    private fun handleLocalidadesResult(result: Result<List<com.example.controllerapp.data.remote.model.LocalidadInfo>>) {
        when (result) {
            is Result.Success -> {
                if (result.data.isEmpty()) {
                    showEmptyState()
                } else {
                    showLocalidades(result.data)
                }
            }
            is Result.Error -> {
                showError(result.message)
            }
            is Result.Loading -> {
                // Loading se maneja por separado
            }
        }
    }
    
    /**
     * Muestra las localidades en el RecyclerView
     */
    private fun showLocalidades(localidades: List<com.example.controllerapp.data.remote.model.LocalidadInfo>) {
        binding.recyclerViewLocalidades.visibility = View.VISIBLE
        binding.tvEmptyState.visibility = View.GONE
        adapter.submitList(localidades)
    }
    
    /**
     * Muestra el estado vacío
     */
    private fun showEmptyState() {
        binding.recyclerViewLocalidades.visibility = View.GONE
        binding.tvEmptyState.visibility = View.VISIBLE
        binding.tvEmptyState.text = "No hay localidades disponibles"
    }
    
    /**
     * Muestra un mensaje de error
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        binding.recyclerViewLocalidades.visibility = View.GONE
        binding.tvEmptyState.visibility = View.VISIBLE
        binding.tvEmptyState.text = message
    }
    
    /**
     * Muestra/oculta el indicador de carga
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
