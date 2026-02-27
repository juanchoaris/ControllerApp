package com.example.controllerapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.controllerapp.ControllerApplication
import com.example.controllerapp.R
import com.example.controllerapp.databinding.ActivityLoginBinding
import com.example.controllerapp.presentation.home.HomeActivity
import com.example.controllerapp.utils.Result
import kotlinx.coroutines.launch

/**
 * Activity de Login
 * Implementa la capa de seguridad: control de versiones y autenticación
 */
class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            (application as ControllerApplication).database
        )
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeViewModel()
        
        // Iniciar proceso de verificación
        viewModel.checkVersion()
    }
    
    /**
     * Configura los elementos de UI
     */
    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            viewModel.authenticate()
        }
    }
    
    /**
     * Observa los cambios en el ViewModel
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.versionState.collect { result ->
                handleVersionResult(result)
            }
        }
        
        lifecycleScope.launch {
            viewModel.loginState.collect { result ->
                handleLoginResult(result)
            }
        }
        
        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                showLoading(isLoading)
            }
        }
    }
    
    /**
     * Maneja el resultado de la verificación de versión
     */
    private fun handleVersionResult(result: Result<String>) {
        when (result) {
            is Result.Success -> {
                showVersionDialog(result.data)
            }
            is Result.Error -> {
                showError("Control de Versiones", result.message)
            }
            is Result.Loading -> {
                // Mostrar loading si es necesario
            }
        }
    }
    
    /**
     * Maneja el resultado del login
     */
    private fun handleLoginResult(result: Result<Boolean>) {
        when (result) {
            is Result.Success -> {
                if (result.data) {
                    navigateToHome()
                }
            }
            is Result.Error -> {
                showError("Error de Autenticación", result.message)
            }
            is Result.Loading -> {
                // Mostrar loading se maneja en observeViewModel
            }
        }
    }
    
    /**
     * Muestra el diálogo con información de versión
     */
    private fun showVersionDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Control de Versiones")
            .setMessage(message)
            .setPositiveButton("Continuar") { dialog, _ ->
                dialog.dismiss()
                // Permitir continuar con el login
            }
            .setCancelable(false)
            .show()
    }
    
    /**
     * Muestra un diálogo de error
     */
    private fun showError(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    /**
     * Muestra/oculta el indicador de carga
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
    }
    
    /**
     * Navega a la pantalla Home
     */
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
