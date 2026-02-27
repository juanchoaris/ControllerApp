package com.example.controllerapp.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.controllerapp.ControllerApplication
import com.example.controllerapp.databinding.ActivityHomeBinding
import com.example.controllerapp.presentation.localidades.LocalidadesActivity
import com.example.controllerapp.presentation.tablas.TablasActivity
import kotlinx.coroutines.launch

/**
 * Activity Home
 * Pantalla principal que muestra información del usuario y navegación
 */
class HomeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (application as ControllerApplication).database
        )
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeViewModel()
        
        // Cargar datos del usuario
        viewModel.loadUserData()
    }
    
    /**
     * Configura los elementos de UI
     */
    private fun setupUI() {
        binding.btnTablas.setOnClickListener {
            navigateToTablas()
        }
        
        binding.btnLocalidades.setOnClickListener {
            navigateToLocalidades()
        }
    }
    
    /**
     * Observa los cambios en el ViewModel
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.userState.collect { user ->
                user?.let {
                    binding.tvUsuario.text = "Usuario: ${it.usuario}"
                    binding.tvIdentificacion.text = "Identificación: ${it.identificacion}"
                    binding.tvNombre.text = "Nombre: ${it.nombre}"
                }
            }
        }
    }
    
    /**
     * Navega a la pantalla de Tablas
     */
    private fun navigateToTablas() {
        val intent = Intent(this, TablasActivity::class.java)
        startActivity(intent)
    }
    
    /**
     * Navega a la pantalla de Localidades
     */
    private fun navigateToLocalidades() {
        val intent = Intent(this, LocalidadesActivity::class.java)
        startActivity(intent)
    }
}
