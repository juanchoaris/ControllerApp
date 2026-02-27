package com.example.controllerapp

import android.app.Application
import com.example.controllerapp.data.local.database.AppDatabase

/**
 * Clase Application personalizada
 * Inicializa componentes globales de la aplicación
 */
class ControllerApplication : Application() {
    
    /**
     * Instancia de la base de datos
     * Lazy initialization para optimizar recursos
     */
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
    
    override fun onCreate() {
        super.onCreate()
        // Inicializaciones adicionales si se requieren
    }
}
