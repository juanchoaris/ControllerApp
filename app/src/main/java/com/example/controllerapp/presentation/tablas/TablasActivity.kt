package com.example.controllerapp.presentation.tablas

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controllerapp.ControllerApplication
import com.example.controllerapp.databinding.ActivityTablasBinding
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.launch

/**
 * Activity de Tablas
 * Exhibe la información de las tablas obtenidas del esquema
 */
class TablasActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTablasBinding
    private val viewModel: TablasViewModel by viewModels {
        TablasViewModelFactory(
            (application as ControllerApplication).database
        )
    }
    private lateinit var adapter: TablasAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTablasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Tablas del Esquema"
        
        setupRecyclerView()
        observeViewModel()
        
        // Cargar tablas
        viewModel.loadTables()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    /**
     * Configura el RecyclerView
     */
    private fun setupRecyclerView() {
        adapter = TablasAdapter()
        binding.recyclerViewTablas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTablas.adapter = adapter
    }
    
    /**
     * Observa los cambios en el ViewModel
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.tablasState.collect { result ->
                handleTablasResult(result)
            }
        }
        
        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                showLoading(isLoading)
            }
        }
    }
    
    /**
     * Maneja el resultado de la carga de tablas
     */
    private fun handleTablasResult(result: Result<List<com.example.controllerapp.data.local.entity.SchemaTableEntity>>) {
        when (result) {
            is Result.Success -> {
                if (result.data.isEmpty()) {
                    showEmptyState()
                } else {
                    showTables(result.data)
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
     * Muestra las tablas en el RecyclerView
     */
    private fun showTables(tables: List<com.example.controllerapp.data.local.entity.SchemaTableEntity>) {
        binding.recyclerViewTablas.visibility = View.VISIBLE
        binding.tvEmptyState.visibility = View.GONE
        adapter.submitList(tables)
    }
    
    /**
     * Muestra el estado vacío
     */
    private fun showEmptyState() {
        binding.recyclerViewTablas.visibility = View.GONE
        binding.tvEmptyState.visibility = View.VISIBLE
        binding.tvEmptyState.text = "No hay tablas almacenadas"
    }
    
    /**
     * Muestra un mensaje de error
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    /**
     * Muestra/oculta el indicador de carga
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
